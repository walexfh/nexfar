package com.example.model

enum class UserRole(val label: String, val badgeColorHex: Long) {
    PHARMACIST_RT("Farmacêutico RT", 0xFF20A89A),
    ATTENDANT("Atendente / Balconista", 0xFF3B82F6),
    STORE_MANAGER("Gestor de Farmácia", 0xFF8B5CF6),
    SAAS_OWNER("Proprietário NEXFAR", 0xFF142D43)
}

data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val crfNumber: String = "",
    val role: UserRole = UserRole.PHARMACIST_RT,
    val currentOrgId: String,
    val avatarInitials: String = "WF"
)

data class Organization(
    val id: String,
    val name: String,
    val cnpj: String,
    val planId: String,
    val planName: String,
    val activeMembersCount: Int,
    val city: String
)

enum class MedicineTarja(val label: String, val colorHex: Long) {
    LIVRE("Isento (MIP)", 0xFF059669),
    VERMELHA_SEM_RETENCAO("Tarja Vermelha (Sob Prescrição)", 0xFFDC2626),
    VERMELHA_RETENCAO("Tarja Vermelha (Retenção de Receita)", 0xFFB91C1C),
    PRETA("Tarja Preta (Controle Especial)", 0xFF1E293B),
    AMARELA("Genérico", 0xFFD97706)
}

data class Medicine(
    val id: String,
    val tradeName: String,
    val activeIngredient: String,
    val concentration: String,
    val dosageForm: String, // Comprimido, Xarope, Solução Injetável, etc.
    val manufacturer: String,
    val anvisaRegistry: String, // Ex: MS 1.0043.1234.001-2
    val tarja: MedicineTarja,
    val isGeneric: Boolean,
    val pmcPrice: Double, // Preço Máximo ao Consumidor
    val approvedIndications: List<String>,
    val contraindications: List<String>,
    val adverseReactions: List<String>,
    val majorInteractions: List<String>,
    val posologyApproved: String,
    val clinicalWarnings: List<String>,
    val anvisaSource: String = "Bulário Eletrônico ANVISA / Farmacopeia Brasileira 6ª Ed.",
    val lastUpdated: String = "Outubro/2026"
)

enum class CalculatorType(val title: String, val subtitle: String, val formula: String) {
    DROPS_INFUSION(
        "Gotejamento & Infusão",
        "Cálculo de gotas/min e microgotas/min para infusão",
        "Gotas/min = Volume (mL) / (Tempo (h) × 3)"
    ),
    DILUTION(
        "Diluição & Mistura",
        "Cálculo clássico de conservação de massa",
        "C₁ × V₁ = C₂ × V₂"
    ),
    PEDIATRIC_DOSE(
        "Dosagem por Peso (Pediátrico)",
        "Dose recomendada mg/kg/dia para suspensão/solução",
        "Dose Diária = Peso (kg) × Dose (mg/kg) / Tomadas"
    ),
    CREATININE_CLEARANCE(
        "Clearance de Creatinina",
        "Fórmula de Cockcroft-Gault para ajuste renal",
        "ClCr = [(140 - idade) × peso] / (72 × CrSérica) [×0.85 mulheres]"
    ),
    CONCENTRATION_CONVERTER(
        "Conversor de Concentração",
        "Conversão entre % (m/v), mg/mL e proporção (ppm)",
        "1% m/v = 10 mg/mL = 10.000 ppm"
    ),
    UNIT_CONVERTER(
        "Conversor de Unidades Farmacêuticas",
        "Conversão direta entre g, mg, mcg e mL, L",
        "1 g = 1.000 mg = 1.000.000 mcg"
    )
}

data class CalculatorHistoryItem(
    val id: String,
    val type: CalculatorType,
    val inputsSummary: String,
    val result: String,
    val timestamp: String
)

data class KnowledgeArticle(
    val id: String,
    val title: String,
    val category: String, // Farmacologia, Atenção Farmacêutica, Legislação RDC, Fisiologia
    val readTimeMinutes: Int,
    val summary: String,
    val fullContent: String,
    val keyPoints: List<String>,
    val references: List<String>,
    val isFavorite: Boolean = false
)

data class MentalMapStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val isRedFlag: Boolean = false,
    val badgeLabel: String = ""
)

data class Pathology(
    val id: String,
    val name: String,
    val icdCode: String, // CID-10
    val shortSummary: String,
    val symptoms: List<String>,
    val riskFactors: List<String>,
    val firstLineTreatments: List<String>,
    val redFlags: List<String>,
    val referralGuidance: String,
    val mentalMap: List<MentalMapStep>,
    val guidelineReference: String
)

data class QuizQuestion(
    val id: String,
    val category: String,
    val questionText: String,
    val options: List<String>,
    val correctIndex: Int,
    val technicalExplanation: String,
    val regulatoryReference: String
)

data class PatientRecord(
    val id: String,
    val name: String,
    val age: Int,
    val systolicBP: Int = 120,
    val diastolicBP: Int = 80,
    val bloodGlucose: Int = 95, // mg/dL
    val weightKg: Double = 72.0,
    val heightCm: Int = 170,
    val continuousMedicines: List<String>,
    val notes: String,
    val lastVisitDate: String = "28/10/2026",
    val statusBadge: String = "Controlado"
)

enum class PlanTier(
    val planName: String,
    val priceMonthly: String,
    val billingNote: String,
    val targetAudience: String,
    val features: List<String>,
    val isHighlighted: Boolean = false
) {
    INDIVIDUAL(
        "INDIVIDUAL",
        "R$ 19,90",
        "por mês",
        "Estudantes, balconistas e autônomos",
        listOf(
            "Acesso completo ao Bulário ANVISA",
            "Central com todas as 6 calculadoras",
            "Assistente IA Farmacêutico (100 consultas/mês)",
            "Simulados ilimitados com ranking",
            "Modo offline para consultas salvas"
        )
    ),
    PROFESSIONAL(
        "PROFISSIONAL",
        "R$ 39,90",
        "por mês",
        "Farmacêuticos RT e consultores clínicos",
        listOf(
            "Tudo do plano Individual",
            "Caderno de Atenção Farmacêutica (500 pacientes)",
            "Assistente IA com checagem profunda de interações",
            "Mapas Mentais Clínicos e Condutas ANVISA",
            "Emissão de fichas de conciliação medicamentosa",
            "Suporte prioritário via WhatsApp"
        ),
        isHighlighted = true
    ),
    ENTERPRISE(
        "ESTABELECIMENTO",
        "A partir de R$ 99,90",
        "por mês por unidade",
        "Drogarias, farmácias de manipulação e redes",
        listOf(
            "Tudo do plano Profissional",
            "Multi-usuários com controle de permissões por cargo",
            "Painel Administrativo da Drogaria / Filiais",
            "Caderno de Atenção Farmacêutica unificado",
            "Relatórios de produtividade e dispensação",
            "Treinamento de balconistas com métricas de aprendizagem",
            "Gerente de conta dedicado"
        )
    )
}

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val messageText: String,
    val technicalSummary: String = "",
    val mechanismOfAction: String = "",
    val safetyAlerts: List<String> = emptyList(),
    val citations: List<String> = emptyList(),
    val timestamp: String = "Agora"
)

data class SaaSAdminMetrics(
    val mrrFormatted: String = "R$ 48.750,00",
    val totalUsers: Int = 1420,
    val activeUsersDaily: Int = 890,
    val activeOrganizations: Int = 186,
    val activeSubscriptions: Int = 382,
    val monthlyChurnRate: String = "1.8%",
    val aiTokensConsumed: String = "482.400",
    val systemUptime: String = "99.94%"
)

data class AuditLogEntry(
    val id: String,
    val action: String,
    val actor: String,
    val organization: String,
    val timeAgo: String,
    val status: String = "Sucesso"
)
