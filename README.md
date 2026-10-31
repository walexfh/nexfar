# NEXFAR — Plataforma SaaS Farmacêutica

> **"Inteligência farmacêutica ao alcance de todos"**

NEXFAR é uma plataforma SaaS profissional desenvolvida especificamente para o mercado farmacêutico brasileiro, oferecendo ferramentas clínicas e operacionais para balconistas, atendentes, farmacêuticos responsáveis técnicos (RT), gestores de drogarias e estudantes.

---

## 🎨 Identidade Visual & Conceito
- **Azul-marinho Nobre:** `#142D43` (Segurança, autoridade e solidez clínica)
- **Verde-água Tecnológico:** `#20A89A` (Inovação, saúde e precisão)
- **Fundo Gelo:** `#F4F7FA` (Leveza e legibilidade prolongada)
- **Branco Puro:** `#FFFFFF` (Cards e superfícies)
- **Logotipo Original:** Combinação de cruz farmacêutica estilizada, elemento molecular e circuitos de inteligência digital.

---

## 📱 Módulos Implementados

### 1. Dashboard Inicial (Início)
- Saudação personalizada com identificação do usuário e CRF
- Seletor de Estabelecimento Multi-Tenant (Farmácia São Paulo, Drogarias FarmaVida, Consultório Clínico)
- Campo de busca geral unificada
- Destaque interativo para o Assistente Farmacêutico IA
- Atalhos rápidos para as ferramentas essenciais
- Carrossel de medicamentos consultados recentemente com status regulatório e PMC
- Progresso de aprendizagem em simulados e legislação ANVISA

### 2. Assistente Farmacêutico com IA
- Interface conversacional moderna
- Consultas com chips de perguntas rápidas (Interação Fluoxetina + Tramadol, Ajuste de Ciprofloxacino em Idosos, Comparativo Dipirona vs Paracetamol)
- Respostas estruturadas em:
  - Síntese Técnica
  - Mecanismo Farmacológico
  - Alertas de Segurança e Red Flags
  - Citações Oficiais (ANVISA, Farmacopeia Brasileira, Micromedex)
- Botão de cópia rápida para prontuário ou orientação ao cliente
- Disclaimer explícito de apoio à decisão clínica

### 3. Bulário & Consulta de Medicamentos
- Base demonstrativa com dados da ANVISA (Clavulin, Losartana, Glifage XR, Rivotril, Novalgina, Losec Mups)
- Filtros por tarja regulatória (Livre/MIP, Vermelha sem Retenção, Vermelha Retenção, Preta, Genérico)
- Ficha técnica completa com abas:
  - Registro MS, Fabricante e PMC (Preço Máximo ao Consumidor)
  - Posologia Oficial Aprovada
  - Interações Medicamentosas Críticas
  - Cuidados e Advertências ANVISA

### 4. Central de Calculadoras Clínicas
- **Gotejamento & Infusão:** Cálculo de macrogotas/min e microgotas/min a partir de volume e tempo
- **Diluição & Mistura:** Conservação de massa ($C_1 \times V_1 = C_2 \times V_2$) com cálculo do volume de diluente
- **Dosagem Pediátrica por Peso:** Dose diária total ($mg/kg/dia$), dose por tomada e volume em $mL$ do xarope
- **Clearance de Creatinina (Cockcroft-Gault):** Ajuste renal por idade, peso, creatinina sérica e sexo
- **Conversor de Concentrações:** $\% (m/v) \leftrightarrow mg/mL \leftrightarrow ppm$
- **Conversor de Unidades:** $g \leftrightarrow mg \leftrightarrow mcg \leftrightarrow mL$
- Histórico de cálculos recentes com carimbo de tempo

### 5. Biblioteca Farmacêutica
- Artigos categorizados (Legislação RDC, Farmacologia Clínica, Atenção ao Balcão, Fisiologia)
- Tempo de leitura estimado, resumo executivo e modal de leitura aprofundada com referências oficiais

### 6. Patologias & Mapas Mentais
- Guia de triagem para Hipertensão Arterial Sistêmica (HAS) e Diabetes Mellitus Tipo 2 (DM2)
- Mapas mentais visuais em etapas numeradas conectadas
- Seção destacada de Sinais Vermelhos (Red Flags) para encaminhamento emergencial para SAMU / Pronto Atendimento

### 7. Simulados & Treinamento Contínuo
- Banco de questões categorizadas sobre RDC 20/2011, Portaria 344/98, Farmacologia e Cálculos
- Resposta interativa com pontuação em tempo real, validação imediata e explicação regulatória fundamentada
- Relatório de desempenho final com taxa de acertos e opção de reinício

### 8. Caderno de Atenção Farmacêutica (Acompanhamento de Pacientes)
- Registro de pacientes e histórico de consultas
- Monitoramento de Pressão Arterial com classificação segundo a Sociedade Brasileira de Cardiologia (SBC)
- Rastreamento de Glicemia Capilar e cálculo automático de IMC ($kg/m^2$)
- Conciliação medicamentosa de uso contínuo
- Formulário para cadastro de novos pacientes com conformidade à LGPD (Lei 13.709/2018)

### 9. Planos & Assinaturas SaaS
- **INDIVIDUAL:** R$ 19,90/mês — Estudantes, balconistas e autônomos
- **PROFISSIONAL:** R$ 39,90/mês — Farmacêuticos RT e consultores clínicos
- **ESTABELECIMENTO:** A partir de R$ 99,90/mês por unidade — Drogarias, farmácias de manipulação e redes
- Modal de contratação demonstrativo com alternância entre faturamento mensal e anual (15% OFF)

### 10. Painel Administrativo do Proprietário (SaaS Admin)
- Dashboard executivo independente:
  - MRR: **R$ 48.750,00** (+14.2% ao mês)
  - 382 Assinaturas Ativas
  - 1.420 Usuários cadastrados (890 ativos hoje)
  - Monitoramento de consumo de IA e custo operacional
- Gestão de Estabelecimentos clientes e filiais
- Gestão de Usuários (RTs e atendentes)
- Logs de Auditoria de segurança

---

## 🏗️ Arquitetura Multi-Tenant
O sistema isola o contexto conforme o perfil ativo:
1. **Farmácia São Paulo — Matriz:** Ambiente corporativo para equipe de loja
2. **Drogarias FarmaVida — Filial Jardins:** Ambiente de rede com controle de filiais
3. **Consultório Dr. Walex Ferreira:** Ambiente para profissional autônomo
4. **NEXFAR SaaS Headquarters:** Acesso Master Global ao Painel do Proprietário

---

## 🛠️ Tecnologias & Compatibilidade Android / Web
- **Android Nativo:** Kotlin, Jetpack Compose, Material 3, Coroutines, StateFlow, Navigation
- **Web / Capacitor:** Preparado em `/capacitor.config.ts` e `/web` com React, TypeScript, Vite, Tailwind CSS e Lucide Icons
- **Tipagem Completa:** 18 modelos de domínio em TypeScript prontos para integração com Supabase
