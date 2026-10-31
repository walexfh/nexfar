import { Medicine, Organization, Pathology, QuizQuestion, KnowledgeArticle } from '../types';

export const organizations: Organization[] = [
  {
    id: "org_1",
    name: "Farmácia São Paulo — Matriz",
    cnpj: "12.345.678/0001-90",
    licenseNumber: "AFE 1.23456.7",
    planId: "enterprise",
    ownerUserId: "usr_001",
    city: "São Paulo",
    state: "SP",
    createdAt: "2026-01-10T10:00:00Z"
  },
  {
    id: "org_2",
    name: "Drogarias FarmaVida — Filial Jardins",
    cnpj: "98.765.432/0002-11",
    licenseNumber: "AFE 2.34567.8",
    planId: "enterprise",
    ownerUserId: "usr_002",
    city: "Campinas",
    state: "SP",
    createdAt: "2026-02-15T14:30:00Z"
  },
  {
    id: "org_3",
    name: "Consultório Dr. Walex Ferreira (Autônomo)",
    cnpj: "45.123.890/0001-44",
    licenseNumber: "AFE 3.45678.9",
    planId: "professional",
    ownerUserId: "usr_001",
    city: "Ribeirão Preto",
    state: "SP",
    createdAt: "2026-03-01T08:00:00Z"
  },
  {
    id: "org_admin",
    name: "NEXFAR SaaS Headquarters (Proprietário)",
    cnpj: "00.111.222/0001-00",
    licenseNumber: "AFE 0.00000.0",
    planId: "platform_admin",
    ownerUserId: "usr_admin",
    city: "SaaS Cloud BR",
    state: "SP",
    createdAt: "2026-01-01T00:00:00Z"
  }
];

export const medicines: Medicine[] = [
  {
    id: "med_1",
    tradeName: "Clavulin BD 875mg + 125mg",
    activeIngredient: "Amoxicilina + Clavulanato de Potássio",
    concentration: "875 mg + 125 mg",
    dosageForm: "Comprimido Revestido",
    manufacturer: "GlaxoSmithKline Brasil Ltda.",
    anvisaRegistry: "MS 1.0107.0169.006-1",
    tarja: "VERMELHA_RETENCAO",
    isGeneric: false,
    pmcPrice: 112.45,
    approvedIndications: [
      "Infecções do trato respiratório superior (sinusite, otite média)",
      "Infecções do trato respiratório inferior (bronquite bacteriana, pneumonia comunitária)",
      "Infecções do trato urinário e de pele/tecidos moles"
    ],
    contraindications: [
      "Hipersensibilidade a penicilinas, cefalosporinas ou betalactâmicos",
      "Histórico prévio de icterícia colestática associada à amoxicilina"
    ],
    adverseReactions: ["Diarreia (muito comum)", "Náuseas e vômitos", "Candidíase mucocutânea"],
    majorInteractions: [
      "Alopurinol: aumento de erupções cutâneas",
      "Anticoncepcionais orais: potencial redução da eficácia",
      "Varfarina: monitorar INR rigorosamente"
    ],
    posologyApproved: "Adultos: 1 comprimido a cada 12 horas (2x ao dia), preferencialmente no início da refeição.",
    clinicalWarnings: [
      "Receituário de Controle Especial em 2 vias (RDC 20/2011)",
      "Validade da receita: 10 dias corridos",
      "Não fracionar ou mastigar o comprimido revestido"
    ],
    anvisaSource: "Bulário Eletrônico ANVISA",
    lastUpdated: "2026-10-15"
  },
  {
    id: "med_2",
    tradeName: "Losartana Potássica Genérico",
    activeIngredient: "Losartana Potássica",
    concentration: "50 mg",
    dosageForm: "Comprimido Revestido",
    manufacturer: "EMS S/A",
    anvisaRegistry: "MS 1.0235.0689.002-3",
    tarja: "VERMELHA_SEM_RETENCAO",
    isGeneric: true,
    pmcPrice: 18.90,
    approvedIndications: [
      "Tratamento da hipertensão arterial sistêmica primária",
      "Proteção renal em pacientes com diabetes tipo 2 e proteinúria",
      "Redução do risco cardiovascular"
    ],
    contraindications: [
      "Hipersensibilidade ao princípio ativo",
      "Gravidez (Categoria D - toxicidade fetal em 2º e 3º trimestres)",
      "Uso concomitante com alisquireno em diabéticos com ClCr < 60 mL/min"
    ],
    adverseReactions: ["Tontura postural", "Hipotensão ortostática", "Hipercalemia"],
    majorInteractions: [
      "Diuréticos poupadores de potássio: risco de hipercalemia grave",
      "AINEs (Ibuprofeno): atenuam efeito anti-hipertensivo e aumentam risco de lesão renal"
    ],
    posologyApproved: "Dose inicial: 50 mg uma vez ao dia. Se necessário, titular até 100 mg/dia.",
    clinicalWarnings: [
      "Aferição periódica de Pressão Arterial e monitoramento de função renal / eletrólitos",
      "Atenção em idosos quanto a risco de hipotensão ao levantar"
    ],
    anvisaSource: "Bulário Eletrônico ANVISA",
    lastUpdated: "2026-09-20"
  },
  {
    id: "med_3",
    tradeName: "Glifage XR 500mg",
    activeIngredient: "Cloridrato de Metformina",
    concentration: "500 mg",
    dosageForm: "Comprimido de Liberação Prolongada",
    manufacturer: "Merck S/A",
    anvisaRegistry: "MS 1.0089.0340.003-9",
    tarja: "VERMELHA_SEM_RETENCAO",
    isGeneric: false,
    pmcPrice: 24.30,
    approvedIndications: [
      "Diabetes Mellitus Tipo 2 em monoterapia ou associado",
      "Prevenção do diabetes em indivíduos com pré-diabetes",
      "Síndrome dos Ovários Policísticos (SOP)"
    ],
    contraindications: [
      "Insuficiência renal grave (Clearance de Creatinina < 30 mL/min)",
      "Insuficiência hepática aguda, alcoolismo ativo ou choque séptico"
    ],
    adverseReactions: ["Sabor metálico", "Náuseas e flatulência", "Diarreia no início"],
    majorInteractions: [
      "Contrastes iodados radiológicos: suspender 48h antes por risco de acidose láctica",
      "Álcool etílico: potencializa acidose láctica"
    ],
    posologyApproved: "1 comprimido de 500 mg ao dia com o jantar. Dose máxima: 2.000 mg/dia.",
    clinicalWarnings: [
      "Ingerir sempre durante ou logo após a refeição noturna",
      "Não mastigar, partir ou triturar o comprimido XR"
    ],
    anvisaSource: "Bulário Eletrônico ANVISA",
    lastUpdated: "2026-08-30"
  },
  {
    id: "med_4",
    tradeName: "Rivotril 2,0mg",
    activeIngredient: "Clonazepam",
    concentration: "2,0 mg",
    dosageForm: "Comprimido Sulcado",
    manufacturer: "Produtos Roche Químicos e Farmacêuticos S.A.",
    anvisaRegistry: "MS 1.0100.0067.009-8",
    tarja: "PRETA",
    isGeneric: false,
    pmcPrice: 36.80,
    approvedIndications: [
      "Distúrbios de ansiedade generalizada, síndrome do pânico e fobia social",
      "Crises epilépticas mioclônicas, ausência e acatisia"
    ],
    contraindications: [
      "Glaucoma agudo de ângulo fechado",
      "Insuficiência respiratória grave ou apneia do sono descompensada"
    ],
    adverseReactions: ["Sonolência diurna", "Lentidão psicomotora", "Comprometimento de memória", "Tolerância"],
    majorInteractions: [
      "Álcool e depressores do SNC: depressão respiratória grave e coma",
      "Opioides: alerta de caixa preta ANVISA / FDA"
    ],
    posologyApproved: "Ansiedade: 0,5 mg a 1,0 mg/dia divididos. Estrita prescrição médica.",
    clinicalWarnings: [
      "Notificação de Receita B (Azul) - Portaria 344/98",
      "Retenção compulsória da receita. Validade de 30 dias no estado de emissão"
    ],
    anvisaSource: "Bulário Eletrônico ANVISA",
    lastUpdated: "2026-10-01"
  },
  {
    id: "med_5",
    tradeName: "Novalgina 1g",
    activeIngredient: "Dipirona Monoidratada",
    concentration: "1.000 mg (1 g)",
    dosageForm: "Comprimido Efervescente / Simples",
    manufacturer: "Sanofi Medley Farmacêutica Ltda.",
    anvisaRegistry: "MS 1.8326.0123.004-5",
    tarja: "LIVRE",
    isGeneric: false,
    pmcPrice: 28.50,
    approvedIndications: [
      "Analgésico e antipirético para dor moderada a intensa e febre alta"
    ],
    contraindications: [
      "Hipersensibilidade a pirazolonas ou histórico de agranulocitose",
      "Deficiência de G6PD ou porfiria hepática aguda"
    ],
    adverseReactions: ["Hipotensão transitória", "Reações cutâneas", "Rara agranulocitose"],
    majorInteractions: [
      "Ciclosporina: redução nos níveis séricos",
      "Clorpromazina: hipotermia grave"
    ],
    posologyApproved: "Adultos: 500 mg a 1.000 mg a cada 6 ou 8 horas. Máximo 4.000 mg/dia.",
    clinicalWarnings: [
      "Medicamento Isento de Prescrição (MIP), recomendado uso por curto período",
      "Buscar atendimento médico se febre persistir por mais de 3 dias"
    ],
    anvisaSource: "Bulário Eletrônico ANVISA",
    lastUpdated: "2026-09-12"
  }
];

export const pathologies: Pathology[] = [
  {
    id: "pat_1",
    name: "Hipertensão Arterial Sistêmica (HAS)",
    icdCode: "CID-10: I10",
    shortSummary: "Elevação sustentada dos níveis pressóricos (PA sistólica ≥ 140 mmHg e/ou diastólica ≥ 90 mmHg).",
    symptoms: ["Geralmente assintomática", "Cefaleia occipital matinal", "Tontura ou zumbido no ouvido"],
    riskFactors: ["Idade avançada", "Excesso de peso", "Ingestão excessiva de sódio", "Sedentarismo", "Histórico familiar"],
    firstLineTreatments: [
      "Inibidores da ECA (Enalapril) ou BRA (Losartana)",
      "Bloqueadores dos canais de cálcio (Anlodipino)",
      "Diuréticos tiazídicos (Hidroclorotiazida, Clortalidona)"
    ],
    redFlags: [
      "PA ≥ 180/120 mmHg com dor torácica irradiada (suspeita de SCA)",
      "Déficit neurológico focal súbito ou perda motora (suspeita de AVC)",
      "Dispneia aguda com estertores (edema agudo de pulmão)"
    ],
    referralGuidance: "Acionar SAMU (192) ou Pronto Atendimento se houver sinal de lesão aguda em órgãos-alvo (Emergência Hipertensiva).",
    mentalMap: [
      { stepNumber: 1, title: "Medição Padronizada", description: "5 min de repouso, braço apoiado na altura do coração", isRedFlag: false, badgeLabel: "Triagem" },
      { stepNumber: 2, title: "Estratificação de Risco", description: "Classificar conforme SBC 2020", isRedFlag: false, badgeLabel: "Estratificação" },
      { stepNumber: 3, title: "Sinais Vermelhos", description: "Checar dor torácica, cefaleia súbita, perda visual", isRedFlag: true, badgeLabel: "Emergência" },
      { stepNumber: 4, title: "Intervenção Farmacêutica", description: "Conferir adesão e uso de anti-inflamatórios/descongestionantes", isRedFlag: false, badgeLabel: "Conduta" },
      { stepNumber: 5, title: "Acompanhamento", description: "Declaração de Serviço Farmacêutico e retorno em 15 dias", isRedFlag: false, badgeLabel: "Seguimento" }
    ],
    guidelineReference: "Diretrizes Brasileiras de Hipertensão Arterial - SBC 2020"
  },
  {
    id: "pat_2",
    name: "Diabetes Mellitus Tipo 2 (DM2)",
    icdCode: "CID-10: E11",
    shortSummary: "Distúrbio metabólico caracterizado por resistência à insulina e deficiência progressiva das células beta.",
    symptoms: ["Poliúria", "Polidipsia", "Polifagia com perda ponderal", "Visão turva"],
    riskFactors: ["Sobrepeso/obesidade (IMC ≥ 25)", "Sedentarismo", "Histórico gestacional", "Síndrome metabólica"],
    firstLineTreatments: [
      "Metformina como primeira linha",
      "Inibidores de SGLT2 (Dapagliflozina) para benefício cardiorrenal",
      "Agonistas GLP-1 (Semaglutida)"
    ],
    redFlags: [
      "Glicemia capilar < 54 mg/dL não responsiva a carboidrato rápido",
      "Hálito cetônico, náuseas e dor abdominal (Cetoacidose)",
      "Letargia e alteração do nível de consciência"
    ],
    referralGuidance: "Encaminhar para pronto-socorro se glicemia > 350 mg/dL com sintomas sistêmicos ou hipoglicemia grave.",
    mentalMap: [
      { stepNumber: 1, title: "Glicemia Capilar", description: "Jejum: 80-130 mg/dL | Pós-prandial: < 180 mg/dL", isRedFlag: false, badgeLabel: "Triagem" },
      { stepNumber: 2, title: "Exame dos Pés", description: "Inspeção de úlceras, calosidades e sensibilidade", isRedFlag: false, badgeLabel: "Prevenção" },
      { stepNumber: 3, title: "Regra dos 15g", description: "Glicemia < 70 mg/dL -> 15g de carboidrato e reteste em 15 min", isRedFlag: true, badgeLabel: "Emergência" },
      { stepNumber: 4, title: "Conciliação Terapêutica", description: "Revisar horário dos antidiabéticos e técnica de injeção", isRedFlag: false, badgeLabel: "Conduta" },
      { stepNumber: 5, title: "Meta HbA1c", description: "Meta < 7% para a maioria dos adultos", isRedFlag: false, badgeLabel: "Seguimento" }
    ],
    guidelineReference: "Diretrizes da Sociedade Brasileira de Diabetes 2023-2024"
  }
];

export const mockPatients = [
  {
    id: "pat_rec_1",
    name: "Dona Maria Helena Silva",
    age: 64,
    systolicBP: 138,
    diastolicBP: 88,
    bloodGlucose: 112,
    weightKg: 68.5,
    heightCm: 158,
    continuousMedicines: ["Losartana 50mg", "Hidroclorotiazida 25mg", "Metformina XR 500mg"],
    notes: "Adesão regular. Orientada quanto a hidratação e restrição de sódio.",
    lastVisitDate: "28/10/2026",
    statusBadge: "Hipertensão Controlada"
  },
  {
    id: "pat_rec_2",
    name: "Sr. Antônio Carlos de Souza",
    age: 71,
    systolicBP: 162,
    diastolicBP: 98,
    bloodGlucose: 186,
    weightKg: 84.0,
    heightCm: 172,
    continuousMedicines: ["Enalapril 20mg", "Glibenclamida 5mg", "AAS 100mg"],
    notes: "Pressão Estágio 2 e glicemia alterada. Refere esquecimento frequente das doses.",
    lastVisitDate: "25/10/2026",
    statusBadge: "Requer Atenção Clínica"
  },
  {
    id: "pat_rec_3",
    name: "Juliana Mendes Paiva",
    age: 32,
    systolicBP: 115,
    diastolicBP: 75,
    bloodGlucose: 88,
    weightKg: 59.0,
    heightCm: 165,
    continuousMedicines: ["Fluoxetina 20mg"],
    notes: "Avaliação de rotina. Nenhuma queixa de interação ou efeitos adversos.",
    lastVisitDate: "22/10/2026",
    statusBadge: "Parâmetros Normais"
  }
];
