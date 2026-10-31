-- ==============================================================================
-- NEXFAR — PLATAFORMA SAAS FARMACÊUTICA
-- Script SQL DDL de Estrutura de Banco de Dados Multi-Tenant para SUPABASE
-- ==============================================================================

-- 1. EXTENSÕES NECESSÁRIAS
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 2. TIPOS CUSTOMIZADOS (ENUMS)
DO $$ BEGIN
    CREATE TYPE user_role AS ENUM ('PHARMACIST_RT', 'ATTENDANT', 'STORE_MANAGER', 'SAAS_OWNER');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE medicine_tarja AS ENUM ('LIVRE', 'VERMELHA_SEM_RETENCAO', 'VERMELHA_RETENCAO', 'PRETA', 'AMARELA');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE subscription_status AS ENUM ('ACTIVE', 'PAST_DUE', 'CANCELED', 'TRIAL');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE payment_status AS ENUM ('PAID', 'PENDING', 'FAILED');
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

-- 3. TABELA DE ORGANIZAÇÕES (MULTI-TENANT)
CREATE TABLE IF NOT EXISTS public.organizations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(20) NOT NULL UNIQUE,
    license_number VARCHAR(100), -- AFE / Vigilância Sanitária
    plan_id VARCHAR(50) DEFAULT 'individual',
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 4. TABELA DE PERFIS DE USUÁRIO
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    crf_number VARCHAR(50),
    role user_role DEFAULT 'ATTENDANT',
    current_organization_id UUID REFERENCES public.organizations(id) ON DELETE SET NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 5. TABELA DE VÍNCULOS MULTI-TENANT (MEMBERSHIPS)
CREATE TABLE IF NOT EXISTS public.memberships (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    organization_id UUID NOT NULL REFERENCES public.organizations(id) ON DELETE CASCADE,
    role user_role NOT NULL DEFAULT 'ATTENDANT',
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    UNIQUE(user_id, organization_id)
);

-- 6. TABELA DE MEDICAMENTOS (BASE ANVISA)
CREATE TABLE IF NOT EXISTS public.medicines (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    trade_name VARCHAR(255) NOT NULL,
    active_ingredient TEXT NOT NULL,
    concentration VARCHAR(100) NOT NULL,
    dosage_form VARCHAR(100) NOT NULL,
    manufacturer VARCHAR(255) NOT NULL,
    anvisa_registry VARCHAR(50) NOT NULL UNIQUE,
    tarja medicine_tarja NOT NULL DEFAULT 'LIVRE',
    is_generic BOOLEAN DEFAULT false,
    pmc_price NUMERIC(10, 2) NOT NULL,
    approved_indications TEXT[] DEFAULT '{}',
    contraindications TEXT[] DEFAULT '{}',
    adverse_reactions TEXT[] DEFAULT '{}',
    major_interactions TEXT[] DEFAULT '{}',
    posology_approved TEXT,
    clinical_warnings TEXT[] DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 7. TABELA DE PACIENTES (CADERNO DE ATENÇÃO FARMACÊUTICA)
CREATE TABLE IF NOT EXISTS public.patients (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID NOT NULL REFERENCES public.organizations(id) ON DELETE CASCADE,
    created_by UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    name VARCHAR(255) NOT NULL,
    age INT NOT NULL,
    systolic_bp INT,
    diastolic_bp INT,
    blood_glucose INT,
    weight_kg NUMERIC(5,2),
    height_cm INT,
    continuous_medicines TEXT[] DEFAULT '{}',
    clinical_notes TEXT,
    status_badge VARCHAR(100) DEFAULT 'Estável',
    last_visit_date DATE DEFAULT CURRENT_DATE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 8. TABELA DE CONVERSAS E IA
CREATE TABLE IF NOT EXISTS public.ai_conversations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    organization_id UUID NOT NULL REFERENCES public.organizations(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.ai_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    conversation_id UUID NOT NULL REFERENCES public.ai_conversations(id) ON DELETE CASCADE,
    is_user BOOLEAN NOT NULL DEFAULT true,
    message_text TEXT NOT NULL,
    technical_summary TEXT,
    mechanism_of_action TEXT,
    safety_alerts TEXT[] DEFAULT '{}',
    citations TEXT[] DEFAULT '{}',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 9. TABELA DE LOGS DE AUDITORIA (COMPLIANCE ANVISA/SNGPC)
CREATE TABLE IF NOT EXISTS public.audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID REFERENCES public.organizations(id) ON DELETE CASCADE,
    actor_user_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    actor_name VARCHAR(255) NOT NULL,
    action TEXT NOT NULL,
    target_resource VARCHAR(255) NOT NULL,
    status VARCHAR(20) DEFAULT 'SUCCESS',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT timezone('utc'::text, now()) NOT NULL
);

-- 10. ROW LEVEL SECURITY (RLS)
ALTER TABLE public.organizations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.memberships ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.medicines ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.patients ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_conversations ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.ai_messages ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.audit_logs ENABLE ROW LEVEL SECURITY;

-- POLÍTICAS BÁSICAS:
-- Medicamentos são de leitura pública para qualquer usuário autenticado
CREATE POLICY "Medicines read access for authenticated" ON public.medicines
    FOR SELECT TO authenticated USING (true);

-- Pacientes isolados por Organização
CREATE POLICY "Tenant isolation for patients" ON public.patients
    FOR ALL TO authenticated
    USING (
        organization_id IN (
            SELECT m.organization_id FROM public.memberships m
            WHERE m.user_id = auth.uid() AND m.status = 'ACTIVE'
        )
    );
