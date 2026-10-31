package com.example.data

import com.example.model.*

object NexfarRepository {

    // Multi-tenant organizations
    val organizations = listOf(
        Organization(
            id = "org_1",
            name = "Farmácia São Paulo — Matriz",
            cnpj = "12.345.678/0001-90",
            planId = "enterprise",
            planName = "Plano Estabelecimento (Rede)",
            activeMembersCount = 8,
            city = "São Paulo, SP"
        ),
        Organization(
            id = "org_2",
            name = "Drogarias FarmaVida — Filial Jardins",
            cnpj = "98.765.432/0002-11",
            planId = "enterprise",
            planName = "Plano Estabelecimento",
            activeMembersCount = 5,
            city = "Campinas, SP"
        ),
        Organization(
            id = "org_3",
            name = "Consultório Dr. Walex Ferreira (Autônomo)",
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

    val currentUser = UserProfile(
        id = "usr_001",
        name = "Dr. Walex Ferreira",
        email = "walex.farmacia@nexfar.com.br",
        crfNumber = "CRF-SP 48.912/RT",
        role = UserRole.PHARMACIST_RT,
        currentOrgId = "org_1"
    )

    // Complete Brazilian ANVISA Medicines Catalog
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
            )
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
            )
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
            )
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
            )
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
            )
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
            )
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
        // C1 * V1 = C2 * V2
        // Return missing value depending on which is 0
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
            name = "Dona Maria Helena Silva",
            age = 64,
            systolicBP = 138,
            diastolicBP = 88,
            bloodGlucose = 112,
            weightKg = 68.5,
            heightCm = 158,
            continuousMedicines = listOf("Losartana Potássica 50mg (1x ao dia)", "Hidroclorotiazida 25mg (1x pela manhã)", "Metformina XR 500mg (1x ao jantar)"),
            notes = "Paciente refere boa adesão. Orientada quanto a hidratação e evitar ingestão excessiva de sódio. Pressão em redução favorável.",
            lastVisitDate = "28/10/2026",
            statusBadge = "Hipertensão Controlada"
        ),
        PatientRecord(
            id = "pat_rec_2",
            name = "Sr. Antônio Carlos de Souza",
            age = 71,
            systolicBP = 162,
            diastolicBP = 98,
            bloodGlucose = 186,
            weightKg = 84.0,
            heightCm = 172,
            continuousMedicines = listOf("Enalapril 20mg (2x ao dia)", "Glibenclamida 5mg (1x ao café)", "AAS 100mg"),
            notes = "ALERTA: Pressão Estágio 2 e glicemia elevada. Relatou esquecimento frequente de doses nos fins de semana. Orientado sobre risco e agendado retorno com médico assistente.",
            lastVisitDate = "25/10/2026",
            statusBadge = "Requer Atenção Clínica"
        ),
        PatientRecord(
            id = "pat_rec_3",
            name = "Juliana Mendes Paiva",
            age = 32,
            systolicBP = 115,
            diastolicBP = 75,
            bloodGlucose = 88,
            weightKg = 59.0,
            heightCm = 165,
            continuousMedicines = listOf("Fluoxetina 20mg (1x pela manhã)"),
            notes = "Avaliação de rotina. Nenhuma queixa de interação ou sonolência. Parâmetros hemodinâmicos e metabólicos excelentes.",
            lastVisitDate = "22/10/2026",
            statusBadge = "Parâmetros Normais"
        )
    )

    // AI Assistant preset conversations & clinical logic
    val initialChatMessages = listOf(
        ChatMessage(
            id = "msg_1",
            isUser = false,
            messageText = "Olá, Dr. Walex! Sou o assistente farmacêutico clínico da NEXFAR. Como posso auxiliar na sua tomada de decisão hoje?",
            technicalSummary = "Módulo de apoio à decisão clínica e verificação de interações medicamentosas ativo.",
            citations = listOf("ANVISA Bulário Eletrônico", "Farmacopeia Brasileira 6ª Ed.", "Micromedex Solutions")
        )
    )

    // Admin metrics
    val adminMetrics = SaaSAdminMetrics()

    val auditLogs = listOf(
        AuditLogEntry("log_1", "Alteração de Plano da Farmácia Jardins para Enterprise", "Admin SaaS (walex@nexfar.com.br)", "NEXFAR SaaS", "Há 12 min"),
        AuditLogEntry("log_2", "Dispensação de Antimicrobiano escriturada no SNGPC", "Farmacêutico RT - CRF 48.912", "Farmácia São Paulo", "Há 34 min"),
        AuditLogEntry("log_3", "Novo paciente cadastrado no Caderno de Atenção", "Atendente Lucas R.", "Drogarias FarmaVida", "Há 1 hora"),
        AuditLogEntry("log_4", "Atualização de Bula ANVISA sincronizada (Clavulin)", "Sistema Automático Nexfar Bot", "Global", "Há 3 horas")
    )

    fun generateClinicalAIResponse(prompt: String): ChatMessage {
        val lower = prompt.lowercase()
        return when {
            lower.contains("fluoxetina") && lower.contains("tramadol") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "A associação entre Fluoxetina e Tramadol apresenta risco clinicamente significativo de Síndrome Serotoninérgica grave e redução do limiar convulsivo.",
                    technicalSummary = "INTERAÇÃO GRAVE / CONTRAINDICAÇÃO RELATIVA: A fluoxetina é um potente inibidor da isoenzima CYP2D6 e potencializa a transmissão serotoninérgica no SNC.",
                    mechanismOfAction = "1. Inibição Farmacocinética: O tramadol é um pró-fármaco que depende da CYP2D6 para conversão no metabólito M1 (ativo, 300x mais potente na afinidade ao receptor mu-opioide). A inibição pela fluoxetina reduz a eficácia analgésica.\n2. Sinergismo Farmacodinâmico: Ambos bloqueiam a recaptação neuronal de serotonina (5-HT), precipitando acúmulo sináptico excessivo.",
                    safetyAlerts = listOf(
                        "Alerta de Síndrome Serotoninérgica: hipertermia, clônus, agitação, tremor, diaforese",
                        "Risco aumentado de convulsões, especialmente em idosos ou histórico epiléptico",
                        "Conduta: Recomenda-se evitar a associação ou substituir por analgésico de mecanismo alternativo sob supervisão médica"
                    ),
                    citations = listOf("ANVISA - Farmacovigilância", "Lexicomp Drug Information", "FDA Black Box Warnings")
                )
            }
            lower.contains("ciprofloxacino") && (lower.contains("idoso") || lower.contains("renal")) -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "O uso de Ciprofloxacino em pacientes idosos requer ajuste rigoroso pela função renal (Clearance de Creatinina) e monitoramento de segurança musculoesquelética.",
                    technicalSummary = "ALERTA GERIÁTRICO & AJUSTE RENAL: As quinolonas apresentam excreção predominantemente renal (40-50% inalterada na urina).",
                    mechanismOfAction = "Em idosos com ClCr < 50 mL/min, a meia-vida sérica é prolongada consideravelmente. Além disso, há risco elevado de tendinite e ruptura do tendão de Aquiles potencializado pelo uso concomitante de corticosteroides.",
                    safetyAlerts = listOf(
                        "Se ClCr 30-50 mL/min: administrar 250 a 500 mg a cada 12 horas",
                        "Se ClCr < 30 mL/min: administrar 250 a 500 mg a cada 18 ou 24 horas",
                        "Manter hidratação adequada para prevenir cristalúria em pH urinário alcalino"
                    ),
                    citations = listOf("Diretrizes de Terapêutica Antimicrobiana Sanford Guide", "Bula Oficial ANVISA Ciprofloxacino")
                )
            }
            lower.contains("dipirona") || lower.contains("paracetamol") -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Diferenças Farmacológicas e de Segurança entre Dipirona Sódica e Paracetamol no Balcão:",
                    technicalSummary = "COMPARAÇÃO CLÍNICA: Dipirona é pirazolona com ação analgésica, antipirética e espasmolítica suave. Paracetamol é derivado do p-aminofenol com ação central.",
                    mechanismOfAction = "- Dipirona: Inibição de COX central e periférica, além de ação sobre canais de potássio dependentes de ATP. Excelente para dores cólicas e febre alta.\n- Paracetamol: Inibição seletiva de COX no SNC com mínima ação anti-inflamatória periférica. Medicamento de primeira escolha para gestantes (Categoria B).",
                    safetyAlerts = listOf(
                        "Dipirona: risco raro de agranulocitose idiopática; contraindicada em porfiria e deficiência de G6PD",
                        "Paracetamol: dose máxima estrita de 4.000 mg/dia em adultos (3.000 mg em hepatopatas) para evitar necrose hepática por NAPQI",
                        "Orientar nunca combinar múltiplos antigripais contendo paracetamol oculto"
                    ),
                    citations = listOf("Farmacopeia Brasileira", "Conselho Federal de Farmácia (CFF) - Linha de Cuidado da Dor")
                )
            }
            else -> {
                ChatMessage(
                    id = "resp_${System.currentTimeMillis()}",
                    isUser = false,
                    messageText = "Análise farmacêutica sobre: \"$prompt\". Informações estruturadas de acordo com as normas da ANVISA e literatura clínica internacional.",
                    technicalSummary = "Triagem técnica de suporte à conduta farmacêutica e dispensação orientada.",
                    mechanismOfAction = "A farmacoterapia requer avaliação individualizada de posologia, função renal/hepática e adesão do paciente ao tratamento contínuo.",
                    safetyAlerts = listOf(
                        "Verificar receituário oficial com carimbo, CRM e validade regulamentar",
                        "Orientar paciente sobre horários das tomadas com ou sem alimentos",
                        "Aviso: Informação para suporte técnico, não substitui consulta médica formal"
                    ),
                    citations = listOf("ANVISA - Bulário Eletrônico", "Diretrizes Clínicas Brasileiras", "Micromedex Solutions")
                )
            }
        }
    }
}
