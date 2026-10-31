package com.example.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.model.*

object NexfarRepository {

    // Multi-tenant organizations
    val organizations = listOf(
        Organization(
            id = "org_1",
            name = "Minha Drogaria / Estabelecimento",
            cnpj = "00.000.000/0001-00",
            planId = "enterprise",
            planName = "Plano Estabelecimento",
            activeMembersCount = 1,
            city = "São Paulo, SP"
        ),
        Organization(
            id = "org_2",
            name = "Farmácia Central — Filial 1",
            cnpj = "12.345.678/0001-90",
            planId = "enterprise",
            planName = "Plano Estabelecimento (Rede)",
            activeMembersCount = 5,
            city = "Campinas, SP"
        ),
        Organization(
            id = "org_3",
            name = "Consultório Clínico Farmacêutico",
            cnpj = "45.123.890/0001-44",
            planId = "professional",
            planName = "Plano Profissional Clínico",
            activeMembersCount = 1,
            city = "Ribeirão Preto, SP"
        ),
        Organization(
            id = "org_admin",
            name = "NEXFAR SaaS Headquarters (Proprietário)",
            cnpj = "00.111.222/0001-00",
            planId = "platform_admin",
            planName = "Acesso Master Global",
            activeMembersCount = 14,
            city = "SaaS Cloud BR"
        )
    )

    // Dynamic current user with ability to edit, log out, or switch account
    var currentUser by mutableStateOf(
        UserProfile(
            id = "usr_001",
            name = "Farmacêutico / Atendente",
            email = "usuario@farmacia.com.br",
            crfNumber = "CRF a preencher",
            role = UserRole.PHARMACIST_RT,
            currentOrgId = "org_1",
            avatarInitials = "FA"
        )
    )

    var isUserLoggedIn by mutableStateOf(false)

    fun loginUser(name: String, email: String, crf: String, orgName: String, role: UserRole) {
        val initials = name.split(" ")
            .filter { it.isNotBlank() }
            .take(2)
            .map { it.first().uppercaseChar() }
            .joinToString("")
            .ifEmpty { "FA" }

        currentUser = UserProfile(
            id = "usr_${System.currentTimeMillis()}",
            name = name.ifBlank { "Farmacêutico / Atendente" },
            email = email.ifBlank { "usuario@farmacia.com.br" },
            crfNumber = crf.ifBlank { "Não informado" },
            role = role,
            currentOrgId = "org_1",
            avatarInitials = initials
        )
        isUserLoggedIn = true
    }

    fun logoutUser() {
        isUserLoggedIn = false
    }

    // Master Access Accounts for Super Admin / SaaS Owner to manage access & plans
    val accessAccounts = mutableStateListOf(
        UserAccessAccount(
            id = "acc_1",
            userName = "Farmacêutico Responsável (Admin)",
            email = "admin.farmacia@nexfar.com.br",
            crfNumber = "CRF Ativo",
            pharmacyName = "Drogarias FarmaVida",
            cnpj = "98.765.432/0002-11",
            planTier = PlanTier.ENTERPRISE,
            status = "ATIVO",
            dateRegistered = "28/10/2026"
        ),
        UserAccessAccount(
            id = "acc_2",
            userName = "Dra. Beatriz Santos",
            email = "beatriz.farmacia@gmail.com",
            crfNumber = "CRF-MG 31.420",
            pharmacyName = "Consultório Clínico Independente",
            cnpj = "45.123.890/0001-44",
            planTier = PlanTier.PROFESSIONAL,
            status = "ATIVO",
            dateRegistered = "29/10/2026"
        ),
        UserAccessAccount(
            id = "acc_3",
            userName = "Marcos Vinicius (Balconista)",
            email = "marcos.atendente@farmasaopaulo.com",
            crfNumber = "Balconista",
            pharmacyName = "Farmácia São Paulo Matriz",
            cnpj = "12.345.678/0001-90",
            planTier = PlanTier.INDIVIDUAL,
            status = "PENDENTE_APROVACAO",
            dateRegistered = "31/10/2026"
        ),
        UserAccessAccount(
            id = "acc_4",
            userName = "Farmácia Popular Central (Rede)",
            email = "contato@popularcentral.com.br",
            crfNumber = "CRF-RJ 19.880",
            pharmacyName = "Rede Farmácias Popular Central",
            cnpj = "11.222.333/0001-55",
            planTier = PlanTier.ENTERPRISE,
            status = "PENDENTE_APROVACAO",
            dateRegistered = "31/10/2026"
        )
    )

    fun approveAccount(accountId: String, newPlan: PlanTier? = null) {
        val index = accessAccounts.indexOfFirst { it.id == accountId }
        if (index != -1) {
            val acc = accessAccounts[index]
            accessAccounts[index] = acc.copy(
                status = "ATIVO",
                planTier = newPlan ?: acc.planTier
            )
        }
    }

    fun toggleBlockAccount(accountId: String) {
        val index = accessAccounts.indexOfFirst { it.id == accountId }
        if (index != -1) {
            val acc = accessAccounts[index]
            accessAccounts[index] = acc.copy(
                status = if (acc.status == "BLOQUEADO") "ATIVO" else "BLOQUEADO"
            )
        }
    }

    // Complete Brazilian ANVISA Medicines Catalog with Prescription Types & Interchangeability
    val medicinesList = listOf(
        Medicine(
            id = "med_1",
            tradeName = "Clavulin BD 875mg + 125mg",
            activeIngredient = "Amoxicilina + Clavulanato de Potássio",
            concentration = "875 mg + 125 mg",
            dosageForm = "Comprimido Revestido",
            manufacturer = "GlaxoSmithKline Brasil Ltda.",
            anvisaRegistry = "MS 1.0107.0169.006-1",
            tarja = MedicineTarja.VERMELHA_RETENCAO,
            isGeneric = false,
            pmcPrice = 112.45,
            approvedIndications = listOf(
                "Infecções do trato respiratório superior (sinusite, otite média)",
                "Infecções do trato respiratório inferior (bronquite bacteriana, pneumonia comunitária)",
                "Infecções do trato urinário e de pele/tecidos moles"
            ),
            contraindications = listOf(
                "Hipersensibilidade a penicilinas, cefalosporinas ou betalactâmicos",
                "Histórico prévio de icterícia colestática ou disfunção hepática associada à amoxicilina/clavulanato"
            ),
            adverseReactions = listOf("Diarreia (muito comum)", "Náuseas e vômitos", "Candidíase mucocutânea", "Erupções cutâneas leves"),
            majorInteractions = listOf(
                "Alopurinol: aumento de erupções cutâneas",
                "Anticoncepcionais orais: potencial redução da eficácia",
                "Anticoagulantes orais (Varfarina): monitorar INR"
            ),
            posologyApproved = "Adultos e crianças > 40kg: 1 comprimido de 875mg + 125mg a cada 12 horas (2x ao dia), preferencialmente no início da refeição para reduzir desconforto GI.",
            clinicalWarnings = listOf(
                "Receituário de Controle Especial em 2 vias (RDC 20/2011)",
                "Validade da receita: 10 dias a contar da data de emissão",
                "Não fracionar ou mastigar o comprimido revestido"
            ),
            prescriptionType = PrescriptionType.RECEITA_CONTROLE_ESPECIAL,
            referenceBrand = "Clavulin BD",
            genericName = "Amoxicilina + Clavulanato de Potássio",
            similarBrands = listOf("Sigma-Clav", "Amplamox-AC", "Novamox 2x", "Clavulan")
        ),
        Medicine(
            id = "med_2",
            tradeName = "Losartana Potássica Genérico",
            activeIngredient = "Losartana Potássica",
            concentration = "50 mg",
            dosageForm = "Comprimido Revestido",
            manufacturer = "EMS S/A",
            anvisaRegistry = "MS 1.0235.0689.002-3",
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            isGeneric = true,
            pmcPrice = 18.90,
            approvedIndications = listOf(
                "Tratamento da hipertensão arterial sistêmica primária",
                "Proteção renal em pacientes com diabetes tipo 2 e proteinúria",
                "Redução do risco de morbimortalidade cardiovascular"
            ),
            contraindications = listOf(
                "Hipersensibilidade ao princípio ativo",
                "Gravidez (Categoria D - toxicidade fetal em 2º e 3º trimestres)",
                "Uso concomitante com alisquireno em pacientes com diabetes ou insuficiência renal (TFG < 60 mL/min)"
            ),
            adverseReactions = listOf("Tontura postural", "Hipotensão ortostática", "Hipercalemia (aumento de potássio sérico)"),
            majorInteractions = listOf(
                "Diuréticos poupadores de potássio / suplementos de potássio: risco de hipercalemia grave",
                "AINEs (Ibuprofeno, Diclofenaco): atenuam efeito anti-hipertensivo e aumentam risco de lesão renal aguda"
            ),
            posologyApproved = "Dose inicial usual: 50 mg uma vez ao dia. Se necessário, aumentar para 100 mg/dia em dose única ou dividida em duas tomadas.",
            clinicalWarnings = listOf(
                "Orientar aferição periódica de Pressão Arterial e monitorar função renal / eletrólitos",
                "Alerta para idosos sobre risco de hipotensão postural ao levantar"
            ),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            referenceBrand = "Cozaar",
            genericName = "Losartana Potássica",
            similarBrands = listOf("Corus", "Torlos", "Aradois", "Zart")
        ),
        Medicine(
            id = "med_3",
            tradeName = "Glifage XR 500mg",
            activeIngredient = "Cloridrato de Metformina",
            concentration = "500 mg",
            dosageForm = "Comprimido de Liberação Prolongada",
            manufacturer = "Merck S/A",
            anvisaRegistry = "MS 1.0089.0340.003-9",
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            isGeneric = false,
            pmcPrice = 24.30,
            approvedIndications = listOf(
                "Diabetes Mellitus Tipo 2 como monoterapia ou combinado a outros hipoglicemiantes orais",
                "Prevenção do diabetes tipo 2 em indivíduos com pré-diabetes e fatores de risco",
                "Síndrome dos Ovários Policísticos (SOP)"
            ),
            contraindications = listOf(
                "Insuficiência renal moderada/grave (Clearance de Creatinina < 30 mL/min)",
                "Insuficiência hepática aguda, alcoolismo ativo ou intoxicação alcoólica",
                "Qualquer condição com hipóxia tecidual (choque séptico, insuficiência cardíaca descompensada)"
            ),
            adverseReactions = listOf("Desconforto gastrointestinal (sabor metálico, náuseas, flatulência)", "Diarreia no início do tratamento"),
            majorInteractions = listOf(
                "Contrastes iodados radiológicos: suspender metformina 48h antes devido ao risco de acidose láctica",
                "Álcool etílico: potencializa risco de acidose lática"
            ),
            posologyApproved = "Iniciar com 1 comprimido de 500 mg uma vez ao dia com o jantar. Aumentar gradualmente a cada 10-15 dias até dose máxima de 2.000 mg/dia.",
            clinicalWarnings = listOf(
                "Tomar sempre durante ou após a principal refeição noturna",
                "Não mastigar, partir ou triturar o comprimido XR (liberação prolongada)"
            ),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            referenceBrand = "Glifage XR",
            genericName = "Cloridrato de Metformina",
            similarBrands = listOf("Glicefor", "Dimefor", "Metforix", "Formiglic")
        ),
        Medicine(
            id = "med_4",
            tradeName = "Rivotril 2,0mg",
            activeIngredient = "Clonazepam",
            concentration = "2,0 mg",
            dosageForm = "Comprimido Sulcado",
            manufacturer = "Produtos Roche Químicos e Farmacêuticos S.A.",
            anvisaRegistry = "MS 1.0100.0067.009-8",
            tarja = MedicineTarja.PRETA,
            isGeneric = false,
            pmcPrice = 36.80,
            approvedIndications = listOf(
                "Distúrbios de ansiedade generalizada, síndrome do pânico e fobia social",
                "Crises epilépticas mioclônicas, atônicas e ausência típica",
                "Transtorno do humor afetivo bipolar e acatisia"
            ),
            contraindications = listOf(
                "Glaucoma agudo de ângulo fechado",
                "Insuficiência respiratória grave ou apneia do sono descompensada",
                "Miastenia grave e coma"
            ),
            adverseReactions = listOf("Sonolência diurna", "Lentidão psicomotora", "Comprometimento de memória recente", "Tolerância e dependência"),
            majorInteractions = listOf(
                "Álcool e depressores do SNC: sedação profunda, depressão respiratória fatal",
                "Opioides: alerta de caixa preta ANVISA / FDA para coma respiratório",
                "Inibidores da CYP3A4 (Cetoconazol, Claritromicina): aumentam níveis plasmáticos"
            ),
            posologyApproved = "Ansiedade: 0,5 mg a 1,0 mg/dia em doses divididas. Não exceder a dose prescrita pelo médico.",
            clinicalWarnings = listOf(
                "Notificação de Receita B (Azul) - Portaria SVS/MS nº 344/98",
                "Retenção de receita obrigatória. Validade de 30 dias no estado de emissão",
                "Risco de síndrome de abstinência se descontinuado abruptamente"
            ),
            prescriptionType = PrescriptionType.NOTIFICACAO_B_AZUL,
            referenceBrand = "Rivotril",
            genericName = "Clonazepam",
            similarBrands = listOf("Navotrax", "Clopam", "Zepax", "Uni Clonazep")
        ),
        Medicine(
            id = "med_5",
            tradeName = "Novalgina 1g",
            activeIngredient = "Dipirona Monoidratada",
            concentration = "1.000 mg (1 g)",
            dosageForm = "Comprimido Efervescente / Simples",
            manufacturer = "Sanofi Medley Farmacêutica Ltda.",
            anvisaRegistry = "MS 1.8326.0123.004-5",
            tarja = MedicineTarja.LIVRE,
            isGeneric = false,
            pmcPrice = 28.50,
            approvedIndications = listOf(
                "Analgésico e antipirético indicado para dores moderadas a intensas",
                "Febre alta refratária a outros agentes"
            ),
            contraindications = listOf(
                "Hipersensibilidade a pirazolonas ou histórico de agranulocitose por dipirona",
                "Deficiência congênita de G6PD (favismo)",
                "Porfiria hepática aguda intermitente"
            ),
            adverseReactions = listOf("Hipotensão passageira (especialmente infusão rápida)", "Reações cutâneas alérgicas", "Rara agranulocitose"),
            majorInteractions = listOf(
                "Ciclosporina: redução nos níveis sanguíneos de ciclosporina",
                "Clorpromazina: hipotermia grave"
            ),
            posologyApproved = "Adultos e adolescentes > 15 anos: 500 mg a 1.000 mg a cada 6 ou 8 horas. Dose máxima diária de 4.000 mg.",
            clinicalWarnings = listOf(
                "Medicamento Isento de Prescrição (MIP), porém com recomendação de uso por curto período",
                "Orientar paciente a procurar atendimento médico se febre persistir por mais de 3 dias"
            ),
            prescriptionType = PrescriptionType.ISENTO_MIP,
            referenceBrand = "Novalgina",
            genericName = "Dipirona Monoidratada",
            similarBrands = listOf("Anador", "Doralgina", "Dipidor", "Magnopyrol")
        ),
        Medicine(
            id = "med_6",
            tradeName = "Losec Mups 20mg",
            activeIngredient = "Omeprazol Magnésico",
            concentration = "20 mg",
            dosageForm = "Comprimido Revestido com Microesferas",
            manufacturer = "AstraZeneca do Brasil Ltda.",
            anvisaRegistry = "MS 1.1618.0078.001-4",
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            isGeneric = false,
            pmcPrice = 78.20,
            approvedIndications = listOf(
                "Úlcera péptica gástrica e duodenal",
                "Doença do Refluxo Gastroesofágico (DRGE) e esofagite erosiva",
                "Erradicação de H. pylori associado a antibióticos"
            ),
            contraindications = listOf("Hipersensibilidade a inibidores da bomba de prótons (IBP)"),
            adverseReactions = listOf("Cefaleia", "Dor abdominal leve", "Constipação ou diarreia", "Uso prolongado: hipomagnesemia e déficit de B12"),
            majorInteractions = listOf(
                "Clopidogrel: inibição da ativação do clopidogrel pela CYP2C19 (redução de eficácia antiplaquetária)",
                "Cetoconazol / Itraconazol: redução acentuada da absorção fúngica devido ao aumento do pH gástrico"
            ),
            posologyApproved = "Tomar 1 comprimido pela manhã em jejum, cerca de 30 a 60 minutos antes do café da manhã. Deglutir inteiro com água.",
            clinicalWarnings = listOf(
                "Não mastigar as microesferas de MUPS para preservar a proteção gastro-resistente",
                "Evitar automedicação contínua sem investigação de sintomas de alarme (perda de peso, disfagia)"
            ),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            referenceBrand = "Losec Mups",
            genericName = "Omeprazol Magnésico",
            similarBrands = listOf("Peprazol", "Gastrium", "Victrix", "Omeprotec")
        ),
        Medicine(
            id = "med_7",
            tradeName = "Ritalina 10mg",
            activeIngredient = "Cloridrato de Metilfenidato",
            concentration = "10 mg",
            dosageForm = "Comprimido Simples",
            manufacturer = "Novartis Biociências S.A.",
            anvisaRegistry = "MS 1.0068.0080.003-1",
            tarja = MedicineTarja.PRETA,
            isGeneric = false,
            pmcPrice = 84.50,
            approvedIndications = listOf(
                "Transtorno de Déficit de Atenção e Hiperatividade (TDAH) em crianças > 6 anos e adultos",
                "Narcolepsia"
            ),
            contraindications = listOf(
                "Ansiedade grave, tensão e agitação",
                "Glaucoma, hipertireoidismo e arritmias cardíacas graves",
                "Uso concomitante com inibidores da MAO (risco de crise hipertensiva fatal)"
            ),
            adverseReactions = listOf("Insônia e nervosismo", "Diminuição do apetite e perda de peso", "Taquicardia e palpitações"),
            majorInteractions = listOf(
                "IMAO: crise hipertensiva grave (aguardar 14 dias)",
                "Anti-hipertensivos: metilfenidato pode reduzir a eficácia anti-hipertensiva"
            ),
            posologyApproved = "Iniciar com 5 mg a 10 mg 1 a 2 vezes ao dia (ao café da manhã e almoço). Dose máxima de 60 mg/dia.",
            clinicalWarnings = listOf(
                "Notificação de Receita A (Amarela) - Portaria SVS/MS 344/98 Lista A3",
                "Retenção obrigatória da notificação amarela e receita médica acompanhante",
                "Validade: 30 dias contados a partir da data de emissão em todo o território nacional"
            ),
            prescriptionType = PrescriptionType.NOTIFICACAO_A_AMARELA,
            referenceBrand = "Ritalina",
            genericName = "Cloridrato de Metilfenidato",
            similarBrands = listOf("Concerta", "Attenze", "Ragione")
        ),
        Medicine(
            id = "med_8",
            tradeName = "Tylenol 750mg",
            activeIngredient = "Paracetamol",
            concentration = "750 mg",
            dosageForm = "Comprimido Revestido",
            manufacturer = "Kenvue / Johnson & Johnson",
            anvisaRegistry = "MS 1.5721.1214.001-9",
            tarja = MedicineTarja.LIVRE,
            isGeneric = false,
            pmcPrice = 29.90,
            approvedIndications = listOf(
                "Redução da febre e alívio temporário de dores leves a moderadas (cefaléia, mialgia, odontalgia)",
                "Analgésico seguro de primeira linha para gestantes e lactantes"
            ),
            contraindications = listOf("Hipersensibilidade ao paracetamol ou hepatopatia ativa grave"),
            adverseReactions = listOf("Raras reações cutâneas", "Hepatotoxicidade em superdosagem aguda (> 4g/dia)"),
            majorInteractions = listOf("Álcool: potencialização do metabólito hepatotóxico NAPQI", "Varfarina: monitorar se uso crônico"),
            posologyApproved = "Adultos: 1 comprimido de 750 mg a cada 6 ou 8 horas. Nunca exceder 4.000 mg (4g) em 24 horas.",
            clinicalWarnings = listOf(
                "Medicamento Isento de Prescrição (MIP)",
                "Alerta contra uso associado a outros antigripais contendo paracetamol oculto"
            ),
            prescriptionType = PrescriptionType.ISENTO_MIP,
            referenceBrand = "Tylenol",
            genericName = "Paracetamol",
            similarBrands = listOf("Tyflen", "Parador", "Pacemol", "Termus")
        )
    )

    // Complete Interchangeability Catalog (Guia de Balcão: Ético vs Genérico vs Similar)
    val interchangeableDrugsList = listOf(
        InterchangeableDrug(
            id = "int_1",
            activeIngredient = "Amoxicilina + Clavulanato de Potássio",
            therapeuticClass = "Antibacteriano Betalactâmico com Inibidor de Betalactamase",
            referenceBrand = "Clavulin / Clavulin BD",
            genericName = "Amoxicilina + Clavulanato de Potássio (Genérico)",
            similarBrands = listOf("Sigma-Clav", "Amplamox-AC", "Novamox 2x", "Clavulan"),
            prescriptionType = PrescriptionType.RECEITA_CONTROLE_ESPECIAL,
            tarja = MedicineTarja.VERMELHA_RETENCAO,
            clinicalTip = "Receita em 2 vias branca (RDC 20/2011). Validade de 10 dias. Pode substituir o Ético pelo Genérico ou pelo Similar Equivalente (EQ)."
        ),
        InterchangeableDrug(
            id = "int_2",
            activeIngredient = "Dipirona Monoidratada",
            therapeuticClass = "Analgésico e Antipirético Pirazolônico",
            referenceBrand = "Novalgina",
            genericName = "Dipirona Monoidratada (Genérico)",
            similarBrands = listOf("Anador", "Doralgina", "Dipidor", "Magnopyrol"),
            prescriptionType = PrescriptionType.ISENTO_MIP,
            tarja = MedicineTarja.LIVRE,
            clinicalTip = "MIP (Venda Livre). Não necessita de receita médica. Contraindicada para pacientes com deficiência de G6PD ou porfiria."
        ),
        InterchangeableDrug(
            id = "int_3",
            activeIngredient = "Cloridrato de Metformina",
            therapeuticClass = "Antidiabético Biguanida",
            referenceBrand = "Glifage / Glifage XR",
            genericName = "Cloridrato de Metformina (Genérico)",
            similarBrands = listOf("Glicefor", "Dimefor", "Metforix", "Formiglic"),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            clinicalTip = "Receita Branca Comum. Versão XR (liberação prolongada) não pode ser partida ou mastigada. Tomar sempre com o jantar."
        ),
        InterchangeableDrug(
            id = "int_4",
            activeIngredient = "Clonazepam",
            therapeuticClass = "Ansiolítico e Anticonvulsivante Benzodiazepínico (Lista B1)",
            referenceBrand = "Rivotril",
            genericName = "Clonazepam (Genérico)",
            similarBrands = listOf("Navotrax", "Clopam", "Zepax", "Uni Clonazep"),
            prescriptionType = PrescriptionType.NOTIFICACAO_B_AZUL,
            tarja = MedicineTarja.PRETA,
            clinicalTip = "Notificação de Receita B (Azul) com validade de 30 dias. Retenção obrigatória da notificação no balcão e registro no SNGPC."
        ),
        InterchangeableDrug(
            id = "int_5",
            activeIngredient = "Losartana Potássica",
            therapeuticClass = "Anti-hipertensivo Bloqueador do Receptor de Angiotensina II (BRA)",
            referenceBrand = "Cozaar",
            genericName = "Losartana Potássica (Genérico)",
            similarBrands = listOf("Corus", "Torlos", "Aradois", "Zart"),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            clinicalTip = "Receita Branca Comum. Não associar com poupadores de potássio ou suplementos sem monitoramento de eletrólitos."
        ),
        InterchangeableDrug(
            id = "int_6",
            activeIngredient = "Omeprazol Magnésico",
            therapeuticClass = "Antiulceroso Inibidor da Bomba de Prótons (IBP)",
            referenceBrand = "Losec Mups",
            genericName = "Omeprazol (Genérico)",
            similarBrands = listOf("Peprazol", "Gastrium", "Victrix", "Omeprotec"),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            clinicalTip = "Tomar em jejum pela manhã, 30 min antes do café. Não mastigar as microesferas."
        ),
        InterchangeableDrug(
            id = "int_7",
            activeIngredient = "Cloridrato de Metilfenidato",
            therapeuticClass = "Psicoestimulante do SNC (Lista A3 - Entorpecentes/Psicotrópicos)",
            referenceBrand = "Ritalina / Concerta",
            genericName = "Cloridrato de Metilfenidato (Genérico)",
            similarBrands = listOf("Concerta", "Attenze", "Ragione"),
            prescriptionType = PrescriptionType.NOTIFICACAO_A_AMARELA,
            tarja = MedicineTarja.PRETA,
            clinicalTip = "Notificação de Receita A (Amarela) oficial. Validade nacional de 30 dias. Exige controle rígido de estoque no SNGPC."
        ),
        InterchangeableDrug(
            id = "int_8",
            activeIngredient = "Paracetamol",
            therapeuticClass = "Analgésico e Antipirético não opiáceo",
            referenceBrand = "Tylenol",
            genericName = "Paracetamol (Genérico)",
            similarBrands = listOf("Tyflen", "Parador", "Pacemol", "Termus"),
            prescriptionType = PrescriptionType.ISENTO_MIP,
            tarja = MedicineTarja.LIVRE,
            clinicalTip = "MIP (Venda Livre). Seguro para gestantes (Categoria B). Alerta de dose máxima diária de 4.000 mg."
        ),
        InterchangeableDrug(
            id = "int_9",
            activeIngredient = "Cloridrato de Fluoxetina",
            therapeuticClass = "Antidepressivo Inibidor Seletivo da Recaptação de Serotonina (ISRS - C1)",
            referenceBrand = "Prozac",
            genericName = "Cloridrato de Fluoxetina (Genérico)",
            similarBrands = listOf("Daforin", "Verotina", "Fluxene", "Depress"),
            prescriptionType = PrescriptionType.RECEITA_CONTROLE_ESPECIAL,
            tarja = MedicineTarja.VERMELHA_RETENCAO,
            clinicalTip = "Receita Branca de Controle Especial em 2 vias (Portaria 344/98 Lista C1). Validade de 30 dias."
        ),
        InterchangeableDrug(
            id = "int_10",
            activeIngredient = "Atorvastatina Cálcica",
            therapeuticClass = "Hipolipemiante Inibidor da HMG-CoA Redutase",
            referenceBrand = "Lipitor",
            genericName = "Atorvastatina Cálcica (Genérico)",
            similarBrands = listOf("Citalor", "Plavom", "Zirter", "Torvast"),
            prescriptionType = PrescriptionType.RECEITA_BRANCA_SIMPLES,
            tarja = MedicineTarja.VERMELHA_SEM_RETENCAO,
            clinicalTip = "Receita Branca Comum. Tomar à noite. Orientar relato imediato de mialgias ou dores musculares intensas."
        ),
        InterchangeableDrug(
            id = "int_11",
            activeIngredient = "Ibuprofeno",
            therapeuticClass = "Anti-inflamatório Não Esteroidal (AINE)",
            referenceBrand = "Alivium / Advil",
            genericName = "Ibuprofeno (Genérico)",
            similarBrands = listOf("Advil", "Alivium", "Buscofem", "Dalsy"),
            prescriptionType = PrescriptionType.ISENTO_MIP,
            tarja = MedicineTarja.LIVRE,
            clinicalTip = "Dosagens até 400mg são MIP. Dosagens de 600mg são tarja vermelha com receita simples. Ingerir com alimentos."
        )
    )

    // Calculators computation algorithms
    fun calculateDropsInfusion(volumeMl: Double, timeHours: Double): Pair<Double, Double> {
        if (timeHours <= 0 || volumeMl <= 0) return Pair(0.0, 0.0)
        val macrogotas = volumeMl / (timeHours * 3.0)
        val microgotas = volumeMl / timeHours
        return Pair(macrogotas, microgotas)
    }

    fun calculateDilution(c1: Double, v1: Double, c2: Double, v2: Double): Double? {
        return when {
            v2 == 0.0 && c2 > 0 -> (c1 * v1) / c2
            c2 == 0.0 && v2 > 0 -> (c1 * v1) / v2
            v1 == 0.0 && c1 > 0 -> (c2 * v2) / c1
            c1 == 0.0 && v1 > 0 -> (c2 * v2) / v1
            else -> null
        }
    }

    fun calculatePediatricDose(
        weightKg: Double,
        recommendedDoseMgKgDay: Double,
        syrupConcentrationMgMl: Double,
        takesPerDay: Int
    ): Triple<Double, Double, Double> {
        if (weightKg <= 0 || recommendedDoseMgKgDay <= 0 || takesPerDay <= 0) return Triple(0.0, 0.0, 0.0)
        val totalDailyMg = weightKg * recommendedDoseMgKgDay
        val dosePerTakeMg = totalDailyMg / takesPerDay
        val volumePerTakeMl = if (syrupConcentrationMgMl > 0) dosePerTakeMg / syrupConcentrationMgMl else 0.0
        return Triple(totalDailyMg, dosePerTakeMg, volumePerTakeMl)
    }

    fun calculateCreatinineClearance(
        age: Int,
        weightKg: Double,
        serumCreatinineMgDl: Double,
        isFemale: Boolean
    ): Double {
        if (serumCreatinineMgDl <= 0 || age <= 0 || weightKg <= 0) return 0.0
        var clcr = ((140 - age) * weightKg) / (72.0 * serumCreatinineMgDl)
        if (isFemale) {
            clcr *= 0.85
        }
        return clcr
    }

    // Knowledge Articles
    val knowledgeArticles = listOf(
        KnowledgeArticle(
            id = "art_1",
            title = "Dispensação Segura de Antibióticos — RDC 20/2011 ANVISA",
            category = "Legislação & Regulação",
            readTimeMinutes = 6,
            summary = "Diretrizes obrigatórias para conferência de receituários de controle especial, validade de 10 dias, escrituração no SNGPC e retenção de 2ª via.",
            fullContent = """
                A RDC nº 20/2011 da ANVISA dispõe sobre o controle de medicamentos à base de substâncias classificadas como antimicrobianos, de uso sob prescrição, isoladas ou em associação.
                
                Pontos de Atenção Crítica para o Balconista e Farmacêutico:
                1. Validade da Receita: Exatamente 10 (dez) dias contados a partir da data de sua emissão pelo médico/dentista.
                2. Número de Vias: Emitida obrigatoriamente em 2 (duas) vias legíveis, sendo a 1ª via retida na farmácia e a 2ª via carimbada e devolvida ao paciente.
                3. Dados Obrigatórios: Nome completo do paciente, idade/sexo, nome do medicamento com concentração e forma farmacêutica, posologia detalhada e tempo de tratamento.
                4. Identificação do Prescritor: Assinatura, carimbo legível com CRM/CRO e registro do Conselho Profissional com UF correspondente.
                5. Escrituração: Deve ser registrado no Sistema Nacional de Gerenciamento de Produtos Controlados (SNGPC) ou registro informatizado do estabelecimento.
            """.trimIndent(),
            keyPoints = listOf(
                "Validade improrrogável de 10 dias corridos",
                "Retenção compulsória da 1ª via no estabelecimento",
                "Obrigatoriedade de registro do comprador no verso da receita"
            ),
            references = listOf("ANVISA - RDC nº 20 de 05/05/2011", "Conselho Federal de Farmácia (CFF) - Guia de Prática de Dispensação")
        ),
        KnowledgeArticle(
            id = "art_2",
            title = "Manejo Farmacêutico de Interações com Inibidores de CYP450",
            category = "Farmacologia Clínica",
            readTimeMinutes = 8,
            summary = "Visão aprofundada dos substratos, inibidores e indutores das isoenzimas CYP3A4, CYP2D6 e CYP2C9 no balcão da farmácia.",
            fullContent = """
                O citocromo P450 compreende uma superfamília de hemeproteínas hepáticas e intestinais responsáveis pelo metabolismo de fase I de mais de 75% dos fármacos em uso clínico.
                
                Isoformas Principais:
                - CYP3A4: Metaboliza estatinas (Sinvastatina, Atorvastatina), benzodiazepínicos e bloqueadores de canais de cálcio.
                  * Inibidores potentes: Cetoconazol, Claritromicina, Ritonavir, Suco de Toranja (Grapefruit).
                  * Consequência: Elevação tóxica dos níveis séricos (ex: risco de rabdomiólise com estatinas).
                - CYP2D6: Responsável pela biotransformação de betabloqueadores, antidepressivos (ISRS) e ativação da codeína e tramadol em seus metabólitos ativos.
                  * Pacientes metabolizadores lentos podem ter ineficácia analgésica de pró-fármacos opioides.
                - CYP2C9: Principal via de metabolização da Varfarina e Fenitoína.
                  * Atenção ao associar com Fluconazol ou AINEs devido a alto risco hemorrágico.
            """.trimIndent(),
            keyPoints = listOf(
                "Sinvastatina + Claritromicina = Risco extremo de rabdomiólise",
                "Codeína depende de CYP2D6 para conversão em morfina",
                "Checar sempre polifarmácia em pacientes idosos (> 5 medicamentos)"
            ),
            references = listOf("Goodman & Gilman - As Bases Farmacológicas da Terapêutica 13ª Ed.", "Micromedex Healthcare Series Drug Interactions")
        ),
        KnowledgeArticle(
            id = "art_3",
            title = "Atenção Farmacêutica ao Paciente Hipertenso e Diabético",
            category = "Atenção Farmacêutica",
            readTimeMinutes = 7,
            summary = "Protocolo de triagem clínica no consultório farmacêutico: medida correta da PA, rastreamento de hipoglicemia e adesão à farmacoterapia.",
            fullContent = """
                A consulta farmacêutica em farmácias comunitárias é regulamentada pelas Resoluções CFF nº 585/2013 e 586/2013, permitindo a realização de procedimentos clínicos autorizados.
                
                Parâmetros para Encaminhamento Imediato:
                - Crise Hipertensiva: PA ≥ 180x120 mmHg com sintomas agudos (cefaleia súbita, alteração visual, dor precordial, dispneia).
                - Hipoglicemia Sintomática: Glicemia capilar < 70 mg/dL com sudorese fria, tremores e confusão mental (conduta: regra dos 15g de carboidrato rápido).
                - Hiperglicemia Grave: Glicemia > 300 mg/dL com hálito cetônico, náuseas e polidipsia intensa.
            """.trimIndent(),
            keyPoints = listOf(
                "Classificação da PA pelas Diretrizes Brasileiras de Hipertensão 2020",
                "Aplicação da regra dos 15 para reversão de hipoglicemia leve",
                "Registro em declaração de serviço farmacêutico para o paciente"
            ),
            references = listOf("Diretrizes Brasileiras de Hipertensão Arterial - SBC/SBH 2020", "Diretrizes da Sociedade Brasileira de Diabetes (SBD) 2023-2024")
        )
    )

    // Pathologies & Mental Maps
    val pathologiesList = listOf(
        Pathology(
            id = "pat_1",
            name = "Hipertensão Arterial Sistêmica (HAS)",
            icdCode = "CID-10: I10",
            shortSummary = "Condição clínica multifatorial caracterizada por elevação sustentada dos níveis pressóricos (PA sistólica ≥ 140 mmHg e/ou PA diastólica ≥ 90 mmHg).",
            symptoms = listOf("Frequentemente assintomática (doença silenciosa)", "Cefaleia occipital matinal", "Tontura ou zumbido no ouvido", "Visão turva episódica"),
            riskFactors = listOf("Idade avançada", "Excesso de peso e adiposidade abdominal", "Ingestão elevada de sal (> 5g/dia)", "Sedentarismo e tabagismo", "História familiar de primeiro grau"),
            firstLineTreatments = listOf(
                "Inibidores da ECA (Enalapril, Ramipril) ou BRA (Losartana, Candesartana)",
                "Bloqueadores dos canais de cálcio (Anlodipino)",
                "Diuréticos tiazídicos ou similares (Hidroclorotiazida, Clortalidona, Indapamida)"
            ),
            redFlags = listOf(
                "PA ≥ 180/120 mmHg com dor no peito irradiada (suspeita de SCA)",
                "Déficit neurológico focal súbito, assimetria facial ou perda de força (suspeita de AVC)",
                "Falta de ar aguda com estertores e edema (edema agudo de pulmão)"
            ),
            referralGuidance = "Encaminhar imediatamente para Pronto Atendimento / SAMU (192) na presença de sinais de lesão em órgãos-alvo (Emergência Hipertensiva). Na urgência hipertensiva sem lesão aguda, orientar consulta médica em até 24-48 horas.",
            mentalMap = listOf(
                MentalMapStep(1, "Medição Padronizada", "Repouso de 5 min, braço apoiado na altura do coração, manguito adequado", false, "Triagem"),
                MentalMapStep(2, "Classificação Inicial", "Normal: < 120/80 | Pré-hipertenso: 121-139/81-89 | Estágio 1: 140-159/90-99", false, "Estratificação"),
                MentalMapStep(3, "Checagem de Alertas", "Dor torácica, cefaleia intensa, perda visual, PA > 180/120 mmHg", true, "Sinal Vermelho"),
                MentalMapStep(4, "Intervenção no Balcão", "Revisão de adesão ao tratamento, checagem de uso de AINEs/descongestionantes", false, "Conduta"),
                MentalMapStep(5, "Acompanhamento Contínuo", "Agendar retorno em 15 dias e emissão de Declaração de Serviço Farmacêutico", false, "Seguimento")
            ),
            guidelineReference = "Diretrizes Brasileiras de Hipertensão Arterial - Sociedade Brasileira de Cardiologia (SBC 2020)"
        ),
        Pathology(
            id = "pat_2",
            name = "Diabetes Mellitus Tipo 2 (DM2)",
            icdCode = "CID-10: E11",
            shortSummary = "Distúrbio metabólico progressivo caracterizado por resistência à insulina e deficiência relativa de sua secreção pelas células beta pancreáticas.",
            symptoms = listOf("Poliúria (aumento do volume urinário)", "Polidipsia (sede excessiva)", "Polifagia com perda ponderal inexplicada", "Visão embaçada e cicatrização lenta"),
            riskFactors = listOf("Sobrepeso ou obesidade (IMC ≥ 25 kg/m²)", "Sedentarismo", "Histórico de diabetes gestacional", "Síndrome metabólica e dislipidemia"),
            firstLineTreatments = listOf(
                "Metformina como pilar farmacológico inicial",
                "Inibidores de SGLT2 (Dapagliflozina, Empagliflozina) para benefício cardiorrenal",
                "Agonistas de receptor GLP-1 (Semaglutida, Liraglutida) com potente redução de peso"
            ),
            redFlags = listOf(
                "Glicemia capilar < 54 mg/dL não responsiva a glicose rápida",
                "Hálito cetônico com náuseas/vômitos e dor abdominal (Cetoacidose diabética)",
                "Alteração aguda de nível de consciência ou letargia (Estado Hiperosmolar Hiperglicêmico)"
            ),
            referralGuidance = "Encaminhar para unidade de urgência se glicemia > 350 mg/dL associada a sintomas sistêmicos ou qualquer hipoglicemia com convulsão/inconsciência.",
            mentalMap = listOf(
                MentalMapStep(1, "Glicemia Capilar", "Jejum ideal: 80-130 mg/dL | Pós-prandial (2h): < 180 mg/dL", false, "Triagem"),
                MentalMapStep(2, "Verificação de Pés", "Inspeção de feridas, calosidades e sensibilidade tátil com monofilamento", false, "Prevenção"),
                MentalMapStep(3, "Alerta Hipoglicêmico", "Glicemia < 70 mg/dL com sudorese fria -> Regra dos 15g de carboidrato", true, "Emergência"),
                MentalMapStep(4, "Revisão Posológica", "Conferir técnica de injeção de insulina e horário de tomada dos antidiabéticos orais", false, "Conduta"),
                MentalMapStep(5, "Meta Terapêutica", "Meta de HbA1c < 7.0% para a maioria dos adultos não gestantes", false, "Seguimento")
            ),
            guidelineReference = "Diretrizes da Sociedade Brasileira de Diabetes (SBD 2023-2024)"
        )
    )

    // Quiz Questions Bank
    val quizQuestions = listOf(
        QuizQuestion(
            id = "q_1",
            category = "Legislação Farmacêutica",
            questionText = "De acordo com a RDC nº 20/2011 da ANVISA, qual é o prazo máximo de validade de uma receita médica de antimicrobianos a partir da data de sua emissão?",
            options = listOf(
                "5 dias corridos",
                "10 dias corridos",
                "15 dias úteis",
                "30 dias corridos"
            ),
            correctIndex = 1,
            technicalExplanation = "Conforme o Artigo 5º da RDC 20/2011 da ANVISA, a receita de antimicrobianos tem validade máxima improrrogável de 10 (dez) dias contados a partir da data de emissão.",
            regulatoryReference = "ANVISA RDC nº 20/2011, Artigo 5º"
        ),
        QuizQuestion(
            id = "q_2",
            category = "Farmacologia Clínica",
            questionText = "Qual das seguintes associações medicamentosas apresenta risco elevado de precipitar rabdomiólise grave por inibição do metabolismo de estatinas?",
            options = listOf(
                "Sinvastatina + Claritromicina (inibidor potente da CYP3A4)",
                "Losartana + Hidroclorotiazida",
                "Paracetamol + Cafeína",
                "Amoxicilina + Clavulanato de Potássio"
            ),
            correctIndex = 0,
            technicalExplanation = "A claritromicina é um potente inibidor da isoenzima CYP3A4. Como a sinvastatina é extensamente metabolizada por essa via, sua concentração sérica pode aumentar em até 10 vezes, desencadeando lesão muscular aguda e rabdomiólise.",
            regulatoryReference = "Bula padrão ANVISA e Micromedex Drug-Drug Interactions"
        ),
        QuizQuestion(
            id = "q_3",
            category = "Cálculos Farmacêuticos",
            questionText = "Um médico prescreveu 500 mL de Soro Fisiológico 0,9% para ser infundido em 8 horas por equipo de macrogotas (1 mL = 20 gotas). Qual deve ser o gotejamento por minuto?",
            options = listOf(
                "15 gotas/minuto",
                "21 gotas/minuto",
                "32 gotas/minuto",
                "45 gotas/minuto"
            ),
            correctIndex = 1,
            technicalExplanation = "Fórmula clássica: Gotas/min = Volume (mL) / (Tempo em horas × 3) = 500 / (8 × 3) = 500 / 24 ≈ 20,83 gotas/minuto, que se arredonda clinicamente para 21 gotas/minuto.",
            regulatoryReference = "Farmacopeia Brasileira e Cálculos de Enfermagem / Farmácia Clínica"
        ),
        QuizQuestion(
            id = "q_4",
            category = "Portaria 344/98 (Controlados)",
            questionText = "Medicamentos ansiolíticos pertencentes à Lista B1 da Portaria SVS/MS nº 344/98 (como Clonazepam e Diazepam) devem ser dispensados mediante qual tipo de receituário?",
            options = listOf(
                "Notificação de Receita A (Amarela)",
                "Notificação de Receita B (Azul)",
                "Receita de Controle Especial em 1 via simples",
                "Receituário Antimicrobiano em 2 vias brancas"
            ),
            correctIndex = 1,
            technicalExplanation = "Os psicotrópicos da Lista B1 da Portaria 344/98 exigem retenção da Notificação de Receita 'B' de cor azul, acompanhada da receita médica comum, com validade de 30 dias contados da emissão.",
            regulatoryReference = "Portaria SVS/MS nº 344/1998, Artigo 44"
        )
    )

    // Patient Records for Caderno de Acompanhamento / Atenção Farmacêutica
    val demoPatients = mutableListOf(
        PatientRecord(
            id = "pat_rec_1",
            name = "Paciente Acompanhamento Exemplo",
            age = 58,
            systolicBP = 130,
            diastolicBP = 85,
            bloodGlucose = 105,
            weightKg = 70.0,
            heightCm = 168,
            continuousMedicines = listOf("Losartana Potássica 50mg (1x ao dia)", "Metformina XR 500mg (1x ao jantar)"),
            notes = "Prontuário pronto para registro de consultas reais da farmácia.",
            lastVisitDate = "31/10/2026",
            statusBadge = "Parâmetros Estáveis"
        )
    )

    // AI Assistant preset conversations
    val initialChatMessages = listOf(
        ChatMessage(
            id = "msg_1",
            isUser = false,
            messageText = "Olá! Sou o assistente clínico da NEXFAR. Digite qualquer medicamento, princípio ativo, dosagem, interação ou dúvida regulatória ANVISA para receber a análise imediata.",
            technicalSummary = "Módulo de apoio à decisão clínica e verificação de interações medicamentosas ativo.",
            citations = listOf("ANVISA Bulário Eletrônico", "Farmacopeia Brasileira 6ª Ed.", "Micromedex Solutions")
        )
    )

    // Admin metrics
    val adminMetrics = SaaSAdminMetrics()

    val auditLogs = listOf(
        AuditLogEntry("log_1", "Novo usuário cadastrado na plataforma", "Sistema Automático", "NEXFAR SaaS", "Há 5 min"),
        AuditLogEntry("log_2", "Dispensação de Antimicrobiano escriturada no SNGPC", "Farmacêutico RT", "Minha Drogaria", "Há 22 min"),
        AuditLogEntry("log_3", "Acesso ao Bulário ANVISA sincronizado", "Atendente de Balcão", "Minha Drogaria", "Há 40 min")
    )

    // Robust, dynamic clinical AI reasoning engine
    fun generateClinicalAIResponse(prompt: String): ChatMessage {
        val lower = prompt.lowercase().trim()

        return when {
            // Interação Fluoxetina + Tramadol
            (lower.contains("fluoxetina") && lower.contains("tramadol")) || (lower.contains("tramadol") && lower.contains("serotonin")) -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "A associação entre Fluoxetina e Tramadol apresenta risco clinicamente significativo de Síndrome Serotoninérgica grave e redução do limiar convulsivo.",
                    technicalSummary = "INTERAÇÃO GRAVE / CONTRAINDICAÇÃO RELATIVA: A fluoxetina inibe potentemente a isoenzima CYP2D6 e eleva a transmissão serotoninérgica no SNC.",
                    mechanismOfAction = "1. Inibição Farmacocinética: O tramadol é um pró-fármaco que depende da CYP2D6 para conversão no metabólito M1 (ativo, 300x mais potente na afinidade ao receptor mu-opioide). A inibição pela fluoxetina reduz a eficácia analgésica.\n2. Sinergismo Farmacodinâmico: Ambos bloqueiam a recaptação de serotonina (5-HT), precipitando acúmulo sináptico excessivo.",
                    safetyAlerts = listOf(
                        "Alerta de Síndrome Serotoninérgica: hipertermia, clônus, agitação, tremor, diaforese",
                        "Risco aumentado de convulsões, especialmente em idosos ou histórico epiléptico",
                        "Conduta: Recomenda-se evitar a associação ou substituir por analgésico de mecanismo alternativo sob supervisão médica",
                        "Receituário: Ambos exigem Receita de Controle Especial em 2 vias (Portaria 344/98 Lista C1 e A2)"
                    ),
                    citations = listOf("ANVISA - Farmacovigilância", "Lexicomp Drug Information", "FDA Black Box Warnings")
                )
            }

            // Quinolonas / Ciprofloxacino
            lower.contains("ciprofloxacino") || lower.contains("levofloxacino") || lower.contains("quinolona") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise Clínica de Fluoroquinolonas (Ciprofloxacino / Levofloxacino): Exige ajuste renal estrito e cautela musculoesquelética.",
                    technicalSummary = "ANTIBIÓTICO BACTERICIDA (Inibidor de DNA-girase e Topoisomerase IV). Excreção predominantemente renal.",
                    mechanismOfAction = "Inibe a replicação e transcrição do DNA bacteriano. Apresenta alta biodisponibilidade oral, porém quela fortemente com cátions divalentes e trivalentes (Cálcio, Ferro, Antiácidos com Magnésio/Alumínio), reduzindo absorção em até 85%.",
                    safetyAlerts = listOf(
                        "Ajuste Renal: Se ClCr 30-50 mL/min, administrar a cada 12h; se ClCr < 30 mL/min, a cada 18-24h",
                        "Alerta ANVISA/FDA: Risco de tendinite e ruptura do tendão de Aquiles, especialmente em idosos (> 60 anos) e usuários de corticosteroides",
                        "Espaçar em pelo menos 2 a 4 horas a ingestão de leite, derivados ou suplementos de cálcio/ferro",
                        "Receituário obrigatório: Receita de Controle Especial em 2 vias (RDC 20/2011, validade 10 dias)"
                    ),
                    citations = listOf("Sanford Guide de Terapêutica Antimicrobiana", "ANVISA RDC 20/2011", "Bulário Eletrônico")
                )
            }

            // Amoxicilina / Betalactâmicos / Clavulin
            lower.contains("amoxicilina") || lower.contains("clavulin") || lower.contains("penicilina") || lower.contains("sinusite") || lower.contains("otite") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise Clínica de Amoxicilina e Amoxicilina + Clavulanato: Betalactâmico de amplo espectro para infecções respiratórias e otorrinolaringológicas.",
                    technicalSummary = "ANTIBIÓTICO BACTERICIDA. O clavulanato de potássio atua como inibidor 'suicida' de betalactamases, restaurando a sensibilidade contra cepas produtoras de enzimas.",
                    mechanismOfAction = "Inibe a síntese da parede celular bacteriana por ligação às PBPs (Penicillin-Binding Proteins). Meia-vida de eliminação de 1 a 1,5 horas com excreção renal ativa.",
                    safetyAlerts = listOf(
                        "Contraindicação absoluta: histórico de anafilaxia ou choque por penicilinas e cefalosporinas",
                        "Tomar preferencialmente no início de refeições para minimizar náuseas e diarreia osmótica",
                        "Não interromper o tratamento antes do prazo estipulado pelo médico para evitar resistência bacteriana",
                        "Receituário: Receita de Controle Especial em 2 vias brancas (RDC 20/2011). Validade: 10 dias corridos da emissão"
                    ),
                    citations = listOf("ANVISA - RDC 20/2011", "Diretrizes Brasileiras de Rinossinusite", "Farmacopeia Brasileira")
                )
            }

            // Azitromicina / Macrolídeos
            lower.contains("azitromicina") || lower.contains("claritromicina") || lower.contains("macrolideo") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise Clínica de Azitromicina (Macrolídeo azalídeo): Ação bacteriostática com meia-vida tecidual extremamente prolongada (até 68 horas).",
                    technicalSummary = "ANTIBIÓTICO MACROLÍDEO. Excelente penetração intracelular em macrófagos e fibroblastos, permitindo posologias curtas de 3 a 5 dias.",
                    mechanismOfAction = "Liga-se reversivelmente à subunidade 50S do ribossomo bacteriano, inibindo a transpeptidação e a síntese proteica celular.",
                    safetyAlerts = listOf(
                        "Alerta Cardíaco ANVISA/FDA: Risco de prolongamento do intervalo QT e arritmia ventricular (Torsades de Pointes) em pacientes cardiopatas",
                        "Tomar 1 hora antes ou 2 horas após refeições ricas para garantir absorção estável",
                        "Receituário obrigatório: Receita de Controle Especial em 2 vias (RDC 20/2011). Validade: 10 dias"
                    ),
                    citations = listOf("ANVISA - Alerta de Farmacovigilância QT", "Goodman & Gilman 13ª Ed.")
                )
            }

            // Dipirona vs Paracetamol / Analgésicos
            lower.contains("dipirona") || lower.contains("paracetamol") || lower.contains("novalgina") || lower.contains("tylenol") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise Diferencial: Dipirona Monoidratada vs. Paracetamol na Prática de Balcão:",
                    technicalSummary = "ANALGÉSICOS E ANTIPIRÉTICOS ISENTOS DE PRESCRIÇÃO (MIP). Ação central e espasmolítica comparada.",
                    mechanismOfAction = "- Dipirona: Ação periférica e central inibindo COX-1, COX-2 e COX-3, além de abertura de canais de K+ dependentes de ATP no músculo liso (efeito antiespasmódico em cólicas).\n- Paracetamol: Inibição de COX no SNC com ação antipirética hipotalâmica; quase desprovido de ação anti-inflamatória periférica.",
                    safetyAlerts = listOf(
                        "Paracetamol: Limite máximo estrito de 4.000 mg/dia em adultos (3.000 mg em idosos/hepatopatas) devido à saturação de glutationa e acúmulo de NAPQI hepatotóxico",
                        "Paracetamol é medicamento de primeira escolha e seguro na gestação (Categoria B)",
                        "Dipirona: rara ocorrência de agranulocitose idiopática; contraindicada em porfiria e deficiência de G6PD",
                        "Classificação regulatória: Isentos de Prescrição (MIP), venda livre orientada pelo farmacêutico"
                    ),
                    citations = listOf("Farmacopeia Brasileira 6ª Ed.", "Conselho Federal de Farmácia (CFF) - Linha de Cuidado da Dor")
                )
            }

            // AINEs / Ibuprofeno / Diclofenaco / Nimesulida / Cetoprofeno
            lower.contains("ibuprofeno") || lower.contains("diclofenaco") || lower.contains("nimesulida") || lower.contains("cetoprofeno") || lower.contains("aine") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise Clínica de Anti-inflamatórios Não Esteroidais (AINEs): Riscos renais, gastrointestinais e cardiovasculares.",
                    technicalSummary = "ANTI-INFLAMATÓRIO, ANALGÉSICO E ANTIPIRÉTICO. Inibição da enzima Ciclo-oxigenase (COX-1 constitutiva e/ou COX-2 induzível).",
                    mechanismOfAction = "Bloqueia a conversão do ácido araquidônico em prostaglandinas inflamatórias. A inibição de COX-1 na mucosa gástrica reduz PGE2 e PGI2 protetoras, aumentando secreção ácida.",
                    safetyAlerts = listOf(
                        "Gastrolesividade: Risco de gastrite, úlcera péptica e sangramento digestivo. Ingerir sempre com alimentos e avaliar protetor gástrico (IBP)",
                        "Nefrotoxicidade: Bloqueiam a vasodilatação da arteríola aferente mediada por prostaglandinas, podendo precipitar Lesão Renal Aguda em idosos desidratados",
                        "Interação Crítica: AINEs atenuam o efeito de todos os anti-hipertensivos (IECA, BRA, diuréticos) e aumentam a PA sistólica em 3 a 5 mmHg",
                        "Nimesulida: Contraindicada em menores de 12 anos e limitada a 7 dias pelo risco de hepatotoxicidade aguda grave (Alerta ANVISA)"
                    ),
                    citations = listOf("ANVISA - Alertas de Segurança com AINEs", "Diretrizes Brasileiras de Hipertensão 2020")
                )
            }

            // Losartana / Hipertensão / Enalapril / Anti-hipertensivos
            lower.contains("losartana") || lower.contains("enalapril") || lower.contains("captopril") || lower.contains("anlodipino") || lower.contains("pressao") || lower.contains("hipertens") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Conduta Farmacêutica em Anti-hipertensivos (Bloqueadores SRAA / Bloqueadores de Cálcio):",
                    technicalSummary = "FÁRMACOS DE PRIMEIRA LINHA PARA HIPERTENSÃO ARTERIAL SISTÊMICA (HAS). Cardioproteção e nefroproteção comprovadas.",
                    mechanismOfAction = "- Losartana (BRA): Bloqueia seletivamente os receptores AT1 da angiotensina II, impedindo vasoconstrição e secreção de aldosterona.\n- Enalapril (IECA): Inibe a conversão de angiotensina I em angiotensina II e inibe a degradação de bradicinina (pode causar tosse seca em 10-15%).\n- Anlodipino: Bloqueador de canais de cálcio di-hidropiridínico que causa vasodilatação periférica (alerta de edema maleolar).",
                    safetyAlerts = listOf(
                        "Contraindicação estrita: Gravidez (Categoria D - risco de malformações renais fetais e oligoidrâmnio)",
                        "Monitorar eletrólitos séricos (risco de hipercalemia, especialmente com diuréticos poupadores de potássio)",
                        "Alerta para idosos: Hipotensão ortostática ao se levantar rapidamente da cama ou cadeira",
                        "Receituário: Receita Branca Comum em 1 via simples (Sob Prescrição Médica)"
                    ),
                    citations = listOf("Diretrizes Brasileiras de Hipertensão Arterial - SBC/SBH 2020", "Bula Oficial ANVISA")
                )
            }

            // Metformina / Diabetes / Glifage / Glicemia / Insulina
            lower.contains("metformina") || lower.contains("glifage") || lower.contains("diabetes") || lower.contains("glicemia") || lower.contains("insulina") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Manejo Farmacêutico do Diabetes Mellitus Tipo 2 & Cloridrato de Metformina:",
                    technicalSummary = "HIPOGLICEMIANTE ORAL (BIGUANIDA). Pilar farmacológico de primeira escolha no DM2 com benefício cardioprotetor e ausência de ganho de peso.",
                    mechanismOfAction = "Ativa a proteína quinase ativada por AMP (AMPK), reduzindo a gliconeogênese hepática em até 30% e aumentando a captação periférica de glicose no músculo esquelético.",
                    safetyAlerts = listOf(
                        "Função Renal: Monitorar Clearance de Creatinina. Contraindicada se ClCr < 30 mL/min devido ao risco de acidose láctica potencialmente fatal",
                        "Procedimentos com Contraste Iodado: Suspender a metformina 48 horas antes de exames radiológicos contrastados",
                        "Efeitos GI: Sabor metálico, náuseas e diarreia são comuns no início; orientar início com doses baixas e tomar sempre junto com o jantar",
                        "Comprimidos XR (Liberação Prolongada): Nunca partir, mastigar ou triturar o comprimido",
                        "Receituário: Receita Branca Comum em 1 via simples"
                    ),
                    citations = listOf("Diretrizes da Sociedade Brasileira de Diabetes (SBD 2023-2024)", "ADA Standards of Care")
                )
            }

            // Clonazepam / Rivotril / Benzodiazepínicos / Diazepam / Zolpidem
            lower.contains("clonazepam") || lower.contains("rivotril") || lower.contains("diazepam") || lower.contains("alprazolam") || lower.contains("zolpidem") || lower.contains("ansiedade") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Controle Farmacêutico & Regulatório de Psicotrópicos Benzodiazepínicos (Portaria SVS/MS nº 344/98):",
                    technicalSummary = "MODULADORES ALOSTÉRICOS POSITIVOS DO RECEPTOR GABA-A. Ação ansiolítica, sedativa, miorrelaxante e anticonvulsivante.",
                    mechanismOfAction = "Aumentam a frequência de abertura dos canais de cloreto acoplados ao GABA, hiperpolarizando a membrana neuronal e diminuindo a excitabilidade do SNC.",
                    safetyAlerts = listOf(
                        "Receituário Obrigatório: Notificação de Receita B de cor AZUL (Lista B1 da Portaria 344/98)",
                        "Validade da Notificação: 30 dias contados da data de emissão no estado emissor (quantidade para no máximo 60 dias de tratamento)",
                        "Retenção de Receita e Escrituração obrigatória no SNGPC da drogaria",
                        "Alerta de Segurança: Tolerância e dependência física/psíquica em uso contínuo > 4 semanas. Nunca descontinuar abruptamente (risco de convulsões de rebote)",
                        "Interação Crítica: Associação com álcool ou opioides causa sedação profunda e depressão respiratória fatal (Black Box Warning)"
                    ),
                    citations = listOf("Portaria SVS/MS nº 344/1998", "ANVISA - Guia Prático de Dispensação de Controlados", "Lexicomp")
                )
            }

            // Metilfenidato / Ritalina / Entorpecentes / Amarela
            lower.contains("metilfenidato") || lower.contains("ritalina") || lower.contains("concerta") || lower.contains("tdah") || lower.contains("amarela") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Regulação & Farmacologia do Cloridrato de Metilfenidato (Lista A3 - Portaria 344/98):",
                    technicalSummary = "PSICOESTIMULANTE DO SNC. Tratamento padrão-ouro para TDAH e Narcolepsia. Sujeito a controle especial rigoroso.",
                    mechanismOfAction = "Bloqueia a recaptação de dopamina e noradrenalina nos terminais sinápticos corticais e subcorticais, aumentando a concentração dessas catecolaminas na fenda sináptica e melhorando a atenção e foco executivo.",
                    safetyAlerts = listOf(
                        "Receituário Obrigatório: Notificação de Receita 'A' de cor AMARELA (Lista A3)",
                        "Validade da Receita: 30 dias em todo o território nacional. Acompanhada obrigatoriamente de receita médica comum",
                        "Controle no SNGPC: Escrituração diária compulsória com conferência de lote e RG do portador no balcão",
                        "Efeitos Cardiovasculares: Pode elevar PA e frequência cardíaca; monitorar crianças quanto ao peso e curva de crescimento estatural",
                        "Contraindicação: Arritmias cardíacas, hipertireoidismo, glaucoma e uso concomitante de IMAO"
                    ),
                    citations = listOf("Portaria SVS/MS nº 344/1998, Art. 36", "ANVISA - Bulário Eletrônico", "Diretrizes de TDAH")
                )
            }

            // Omeprazol / IBP / Gastrite / Refluxo
            lower.contains("omeprazol") || lower.contains("pantoprazol") || lower.contains("esomeprazol") || lower.contains("refluxo") || lower.contains("gastrite") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Orientações Clínicas sobre Inibidores da Bomba de Prótons (IBP - Omeprazol / Pantoprazol):",
                    technicalSummary = "ANTIULCEROSO PROTETOR GÁSTRICO. Inibidor irreversível da enzima H+/K+-ATPase nas células parietais gástricas.",
                    mechanismOfAction = "Pró-fármaco que se acumula no canalículo secretor ácido da célula parietal, onde é protonado e se liga covalentemente à H+/K+-ATPase, suprimindo a secreção ácida basal e estimulada em mais de 90%.",
                    safetyAlerts = listOf(
                        "Modo de Tomada Correto: Tomar pela manhã em jejum rigoroso, 30 a 60 minutos antes do café da manhã",
                        "Uso Prolongado (> 1 ano): Risco de hipomagnesemia, déficit de absorção de Vitamina B12 e aumento do risco de fraturas osteoporóticas e infecção por C. difficile",
                        "Interação Crítica: Omeprazol inibe a CYP2C19 e reduz a bioativação antiplaquetária do Clopidogrel. Em cardiopatas, preferir Pantoprazol",
                        "Receituário: Receita Branca Comum em 1 via simples"
                    ),
                    citations = listOf("Farmacopeia Brasileira", "American College of Gastroenterology (ACG Guidelines)", "ANVISA")
                )
            }

            // Corticosteroides / Prednisona / Dexametasona
            lower.contains("prednisona") || lower.contains("dexametasona") || lower.contains("corticoide") || lower.contains("betametasona") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Manejo Seguro de Corticosteroides Sistêmicos (Prednisona / Dexametasona) no Balcão:",
                    technicalSummary = "ANTI-INFLAMATÓRIO E IMUNOSSUPRESSOR GLICOCORTICOIDE. Alta potência na modulação transcricional de citocinas inflamatórias.",
                    mechanismOfAction = "Liga-se aos receptores citoplasmáticos de glicocorticoides, migra ao núcleo celular e inibe a transcrição de NF-kB e AP-1, bloqueando a síntese de IL-1, IL-6, TNF-alfa e fosfolipase A2.",
                    safetyAlerts = listOf(
                        "Horário Fisiológico: Administrar em dose única matinal (entre 7h e 8h) para respeitar o ciclo circadiano do cortisol e minimizar supressão do eixo HPA",
                        "Desmame Obrigatório: Tratamentos > 14 dias exigem redução gradual de dose para prevenir crise de insuficiência adrenal aguda",
                        "Efeitos Metabólicos: Elevação da glicemia em diabéticos e retenção hidrossalina com aumento da Pressão Arterial",
                        "Receituário: Receita Branca Comum em 1 via simples"
                    ),
                    citations = listOf("Goodman & Gilman - As Bases Farmacológicas da Terapêutica", "Diretrizes Clínicas ANVISA")
                )
            }

            // Estatinas / Sinvastatina / Atorvastatina / Colesterol
            lower.contains("sinvastatina") || lower.contains("atorvastatina") || lower.contains("rosuvastatina") || lower.contains("colesterol") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Farmacologia e Segurança das Estatinas (Sinvastatina / Atorvastatina / Rosuvastatina):",
                    technicalSummary = "HIPOLIPEMIANTE INIBIDOR COMPETITIVO DA HMG-CoA REDUTASE. Redução de LDL-colesterol e estabilização de placas ateroscleróticas.",
                    mechanismOfAction = "Inibe a enzima limitante da síntese hepática de colesterol, aumentando a expressão dos receptores de LDL na superfície dos hepatócitos e acelerando a depuração plasmática de LDL.",
                    safetyAlerts = listOf(
                        "Horário: Sinvastatina tem meia-vida curta (2h) e deve ser tomada à noite (pico da síntese hepática). Atorvastatina e Rosuvastatina têm meia-vida longa (> 14h) e podem ser tomadas em qualquer horário",
                        "Miopatia e Rabdomiólise: Risco potencializado pela associação com inibidores potentes da CYP3A4 (Claritromicina, Cetoconazol, Suco de Toranja)",
                        "Orientar paciente a procurar atendimento imediatamente em caso de dor muscular inexplicada, fraqueza ou urina escura",
                        "Receituário: Receita Branca Comum em 1 via simples"
                    ),
                    citations = listOf("Diretrizes Brasileiras de Dislipidemia e Prevenção da Aterosclerose - SBC 2020", "ANVISA")
                )
            }

            // Antidepressivos ISRS / Sertralina / Escitalopram
            lower.contains("sertralina") || lower.contains("escitalopram") || lower.contains("antidepressivo") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Atenção Farmacêutica em Antidepressivos ISRS (Sertralina / Escitalopram / Citalopram):",
                    technicalSummary = "INIBIDORES SELETIVOS DA RECAPTAÇÃO DE SEROTONINA (ISRS). Medicamentos de controle especial da Lista C1.",
                    mechanismOfAction = "Bloqueiam seletivamente a proteína transportadora de serotonina (SERT), prolongando o tempo de permanência da 5-HT na fenda sináptica e induzindo dessensibilização adaptativa de receptores 5-HT1A pré-sinápticos.",
                    safetyAlerts = listOf(
                        "Latência Terapêutica: O efeito antidepressivo/ansiolítico pleno leva de 2 a 4 semanas para se consolidar. Orientar paciente a não abandonar o tratamento",
                        "Sintomas Iniciais: Pode ocorrer náusea passageira e leve piora da ansiedade nos primeiros 5-7 dias",
                        "Receituário Obrigatório: Receita de Controle Especial em 2 vias brancas (Portaria 344/98 Lista C1). Validade de 30 dias contados da emissão",
                        "Descontinuação: Deve ser gradual para evitar síndrome de descontinuação (vertigem, choques elétricos na cabeça, insônia)"
                    ),
                    citations = listOf("Portaria SVS/MS nº 344/1998 Lista C1", "CANMAT Clinical Guidelines for Depression", "ANVISA")
                )
            }

            // Consulta Geral Dinâmica / Qualquer outro medicamento
            else -> {
                val queriedTerm = prompt.replace("?", "").replace("como", "").replace("qual", "").replace("usar", "").trim().capitalize()
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Avaliação Farmacoterapêutica e Regulatória sobre: \"$queriedTerm\".",
                    technicalSummary = "ANÁLISE FARMACOLÓGICA E DE DISPENSAÇÃO CLÍNICA (Bases ANVISA, Farmacopeia Brasileira e Micromedex).",
                    mechanismOfAction = "A farmacoterapia requer verificação de via metabólica (hepática CYP450 vs. excreção renal inalterada), índice terapêutico, adesão do paciente e compatibilidade com tratamentos de uso contínuo.",
                    safetyAlerts = listOf(
                        "Verificação de Receituário: Conferir legibilidade, carimbo e assinatura com CRM/CRO válido e data de emissão dentro do prazo regulamentar",
                        "Tipo de Notificação: Checar se o fármaco pertence às listas da Portaria 344/98 (Receita Azul B para psicotrópicos, Amarela A para entorpecentes ou Branca 2 vias para antimicrobianos RDC 20/2011)",
                        "Orientação ao Paciente: Informar intervalo correto das tomadas, relação com alimentos e conservação adequada da forma farmacêutica (temperatura ambiente vs. refrigeração)",
                        "Rastreamento de Interações: Perguntar se o paciente faz uso concomitante de anti-hipertensivos, hipoglicemiantes, anticoagulantes ou álcool"
                    ),
                    citations = listOf("ANVISA - Bulário Eletrônico Oficial", "Farmacopeia Brasileira 6ª Edição", "Diretrizes Clínicas Nacionais")
                )
            }
        }
    }
}
