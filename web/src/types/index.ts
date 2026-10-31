/**
 * NEXFAR — PLATAFORMA SAAS FARMACÊUTICA
 * Modelos de Domínio TypeScript para Web, Capacitor e Supabase
 */

export type UserRole = 'PHARMACIST_RT' | 'ATTENDANT' | 'STORE_MANAGER' | 'SAAS_OWNER';

export interface User {
  id: string;
  name: string;
  email: string;
  crfNumber?: string;
  role: UserRole;
  currentOrganizationId: string;
  avatarUrl?: string;
  createdAt: string;
  updatedAt: string;
}

export interface Organization {
  id: string;
  name: string;
  cnpj: string;
  licenseNumber: string; // Autorização de Funcionamento ANVISA (AFE)
  planId: string;
  ownerUserId: string;
  city: string;
  state: string;
  createdAt: string;
}

export interface Membership {
  id: string;
  userId: string;
  organizationId: string;
  role: UserRole;
  status: 'ACTIVE' | 'PENDING' | 'REVOKED';
  joinedAt: string;
}

export interface Plan {
  id: string;
  name: 'INDIVIDUAL' | 'PROFISSIONAL' | 'ESTABELECIMENTO';
  priceMonthly: number;
  priceAnnualMonthlyEquivalent: number;
  features: string[];
  maxUsers: number;
  maxPatients: number;
  hasPrioritySupport: boolean;
  hasAdvancedAIInteractions: boolean;
}

export interface Subscription {
  id: string;
  organizationId: string;
  planId: string;
  status: 'ACTIVE' | 'PAST_DUE' | 'CANCELED' | 'TRIAL';
  currentPeriodStart: string;
  currentPeriodEnd: string;
  cancelAtPeriodEnd: boolean;
}

export interface Payment {
  id: string;
  subscriptionId: string;
  amount: number;
  currency: 'BRL';
  method: 'PIX' | 'CREDIT_CARD' | 'BOLETO';
  status: 'PAID' | 'PENDING' | 'FAILED';
  paidAt?: string;
}

export interface ActiveIngredient {
  id: string;
  name: string;
  casNumber?: string;
  cyp450Pathway?: string; // ex: CYP3A4 substrate / inhibitor
  renalAdjustmentNeeded: boolean;
}

export type PrescriptionType = 
  | 'ISENTO_MIP'
  | 'BRANCA_SIMPLES'
  | 'BRANCA_2VIAS_ANTIMICROBIANO'
  | 'BRANCA_2VIAS_C1'
  | 'NOTIFICACAO_B_AZUL'
  | 'NOTIFICACAO_B2_AZUL'
  | 'NOTIFICACAO_A_AMARELA'
  | 'TALIDOMIDA_ESPECIAL';

export interface InterchangeableDrug {
  id: string;
  activeIngredient: string;
  dosage: string;
  referenceDrug: string; // Medicamento de Referência (Ético)
  referenceLab: string;
  genericOptions: string[]; // Genéricos registrados (Lei 9.787/99)
  similarEquivalents: string[]; // Similares Equivalentes (EQ - RDC 58/2014)
  therapeuticClass: string;
  prescriptionType: PrescriptionType;
  prescriptionLabel: string;
  clerkTip: string; // Dica prática para o balconista
}

export interface UserAccessAccount {
  id: string;
  userName: string;
  email: string;
  crfNumber: string;
  pharmacyName: string;
  cnpj: string;
  planTier: 'INDIVIDUAL' | 'PROFISSIONAL' | 'ESTABELECIMENTO';
  status: 'ATIVO' | 'PENDENTE' | 'BLOQUEADO';
  dateRegistered: string;
}

export interface Medicine {
  id: string;
  tradeName: string;
  activeIngredient: string;
  concentration: string;
  dosageForm: string;
  manufacturer: string;
  anvisaRegistry: string;
  tarja: 'LIVRE' | 'VERMELHA_SEM_RETENCAO' | 'VERMELHA_RETENCAO' | 'PRETA' | 'AMARELA';
  prescriptionType: PrescriptionType;
  prescriptionDescription: string;
  referenceBrand?: string;
  genericName?: string;
  similarBrands?: string;
  isGeneric: boolean;
  pmcPrice: number;
  approvedIndications: string[];
  contraindications: string[];
  adverseReactions: string[];
  majorInteractions: string[];
  posologyApproved: string;
  clinicalWarnings: string[];
  anvisaSource: string;
  lastUpdated: string;
}

export interface KnowledgeArticle {
  id: string;
  title: string;
  category: 'Farmacologia' | 'Atenção Farmacêutica' | 'Legislação RDC' | 'Fisiologia';
  readTimeMinutes: number;
  summary: string;
  fullContent: string;
  keyPoints: string[];
  references: string[];
  createdAt: string;
}

export interface MentalMapStep {
  stepNumber: number;
  title: string;
  description: string;
  isRedFlag: boolean;
  badgeLabel: string;
}

export interface Pathology {
  id: string;
  name: string;
  icdCode: string;
  shortSummary: string;
  symptoms: string[];
  riskFactors: string[];
  firstLineTreatments: string[];
  redFlags: string[];
  referralGuidance: string;
  mentalMap: MentalMapStep[];
  guidelineReference: string;
}

export interface Message {
  id: string;
  conversationId: string;
  isUser: boolean;
  messageText: string;
  technicalSummary?: string;
  mechanismOfAction?: string;
  safetyAlerts?: string[];
  citations?: string[];
  createdAt: string;
}

export interface Conversation {
  id: string;
  userId: string;
  title: string;
  isFavorite: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface Calculator {
  id: string;
  slug: 'drops' | 'dilution' | 'pediatric' | 'creatinine' | 'concentration' | 'units';
  name: string;
  formula: string;
  description: string;
}

export interface QuizQuestion {
  id: string;
  category: string;
  questionText: string;
  options: string[];
  correctIndex: number;
  technicalExplanation: string;
  regulatoryReference: string;
}

export interface QuizAttempt {
  id: string;
  userId: string;
  score: number;
  totalQuestions: number;
  percentage: number;
  completedAt: string;
}

export interface LearningProgress {
  userId: string;
  totalCompletedQuizzes: number;
  accuracyRate: number;
  lastStudiedTopic: string;
}

export interface AIUsage {
  id: string;
  organizationId: string;
  userId: string;
  promptTokens: number;
  completionTokens: number;
  costEstimatedBrl: number;
  timestamp: string;
}

export interface AuditLog {
  id: string;
  organizationId: string;
  actorUserId: string;
  actorName: string;
  action: string;
  targetResource: string;
  timestamp: string;
  status: 'SUCCESS' | 'FAILURE';
}
