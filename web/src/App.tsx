import React, { useState } from 'react';
import {
  Activity,
  AlertTriangle,
  ArrowRightLeft,
  BadgeAlert,
  Bot,
  Building2,
  Calculator,
  Calendar,
  Check,
  CheckCircle2,
  ChevronRight,
  ClipboardList,
  Copy,
  ExternalLink,
  FileSpreadsheet,
  FileText,
  Filter,
  GraduationCap,
  HeartPulse,
  HelpCircle,
  Layers,
  LayoutDashboard,
  Lock,
  LogOut,
  Pill,
  Plus,
  RefreshCw,
  Search,
  Send,
  Shield,
  ShieldAlert,
  Sparkles,
  Stethoscope,
  TrendingUp,
  Unlock,
  User,
  UserCheck,
  Users,
  X
} from 'lucide-react';
import {
  organizations,
  medicines,
  pathologies,
  mockPatients,
  interchangeableDrugs,
  initialAccessAccounts
} from './data/mockData';
import { Medicine, Organization, InterchangeableDrug, UserAccessAccount, PrescriptionType } from './types';
import { isSupabaseConfigured } from './lib/supabase';

type Tab = 'dashboard' | 'assistant' | 'medicines' | 'calculators' | 'patients' | 'pathologies' | 'plans' | 'admin';

export default function App() {
  const [currentTab, setCurrentTab] = useState<Tab>('dashboard');
  const [currentOrg, setCurrentOrg] = useState<Organization>(organizations[0]);
  const [isAdminMode, setIsAdminMode] = useState<boolean>(false);
  const [toastMessage, setToastMessage] = useState<string | null>(null);

  // Dynamic Current User (Clean State & Switchable)
  const [currentUser, setCurrentUser] = useState({
    name: "Farmacêutico Responsável",
    email: "farmacia@nexfar.com.br",
    crf: "CRF Ativo",
    role: "Farmacêutico RT",
    pharmacy: "Drogarias FarmaVida",
    isLoggedIn: true
  });
  const [showUserModal, setShowUserModal] = useState(false);
  const [editUserName, setEditUserName] = useState(currentUser.name);
  const [editUserEmail, setEditUserEmail] = useState(currentUser.email);
  const [editUserCrf, setEditUserCrf] = useState(currentUser.crf);
  const [editUserPharmacy, setEditUserPharmacy] = useState(currentUser.pharmacy);
  const [editUserRole, setEditUserRole] = useState(currentUser.role);

  // Bulário search & filter
  const [bularioView, setBularioView] = useState<'catalog' | 'interchangeable'>('catalog');
  const [searchQuery, setSearchQuery] = useState('');
  const [tarjaFilter, setTarjaFilter] = useState<string>('ALL');
  const [prescriptionFilter, setPrescriptionFilter] = useState<string>('ALL');
  const [selectedMedicine, setSelectedMedicine] = useState<Medicine | null>(null);

  // Intercambialidade search
  const [interchangeableSearch, setInterchangeableSearch] = useState('');

  // Master SaaS Access Accounts
  const [accessAccounts, setAccessAccounts] = useState<UserAccessAccount[]>(initialAccessAccounts);
  const [adminSubTab, setAdminSubTab] = useState<'accesses' | 'metrics' | 'audit'>('accesses');

  // Chat Assistant State
  const [messages, setMessages] = useState<Array<{ isUser: boolean; text: string; technical?: string; mechanism?: string; alerts?: string[]; citations?: string[] }>>([
    {
      isUser: false,
      text: `Olá! Sou o assistente clínico da NEXFAR. Como posso auxiliar na sua tomada de decisão farmacêutica e dispensação segura hoje?`,
      technical: "Motor clínico com checagem de interações, posologia e RDCs da ANVISA.",
      citations: ["ANVISA Bulário Eletrônico", "Farmacopeia Brasileira 6ª Ed.", "RDC 20/2011", "Portaria 344/98"]
    }
  ]);
  const [chatInput, setChatInput] = useState('');
  const [isThinking, setIsThinking] = useState(false);

  // Calculators State
  const [calcTab, setCalcTab] = useState<'drops' | 'dilution' | 'pediatric' | 'creatinine'>('drops');
  const [volDrops, setVolDrops] = useState('500');
  const [timeDrops, setTimeDrops] = useState('8');
  
  // Creatinine
  const [creatAge, setCreatAge] = useState('65');
  const [creatWeight, setCreatWeight] = useState('70');
  const [creatSerum, setCreatSerum] = useState('1.2');
  const [creatIsFemale, setCreatIsFemale] = useState(false);

  // Dilution
  const [c1, setC1] = useState('50');
  const [v1, setV1] = useState('10');
  const [c2, setC2] = useState('10');

  // Pediatric
  const [pedWeight, setPedWeight] = useState('18');
  const [pedDose, setPedDose] = useState('50');
  const [pedConc, setPedConc] = useState('50'); // mg/mL
  const [pedTakes, setPedTakes] = useState('3');

  // Patients state
  const [patientList, setPatientList] = useState(mockPatients);
  const [newPatientName, setNewPatientName] = useState('');
  const [newPatientAge, setNewPatientAge] = useState('');
  const [newPatientBP, setNewPatientBP] = useState('');
  const [showAddPatient, setShowAddPatient] = useState(false);

  const showToast = (msg: string) => {
    setToastMessage(msg);
    setTimeout(() => setToastMessage(null), 3000);
  };

  const copyToClipboard = (text: string, label: string) => {
    navigator.clipboard.writeText(text);
    showToast(`${label} copiado com sucesso!`);
  };

  const handleSendChat = (promptText?: string) => {
    const text = promptText || chatInput;
    if (!text.trim()) return;

    const userMsg = { isUser: true, text };
    setMessages(prev => [...prev, userMsg]);
    if (!promptText) setChatInput('');
    setIsThinking(true);

    setTimeout(() => {
      const lower = text.toLowerCase();
      let reply = {
        isUser: false,
        text: `Avaliação clínica para "${text}". Conforme diretrizes da ANVISA e literatura farmacêutica oficial.`,
        technical: "Análise técnica de suporte à conduta clínica e dispensação orientada.",
        mechanism: "A farmacoterapia requer avaliação individualizada de posologia, função renal/hepática e conferência do receituário regulamentar.",
        alerts: [
          "Verificar carimbo profissional, CRM/CRF e prazo de validade da receita",
          "Orientar o paciente sobre horários das tomadas com ou sem alimentos"
        ],
        citations: ["ANVISA - Bulário Eletrônico", "Farmacopeia Brasileira", "Micromedex Solutions"]
      };

      if (lower.includes('clavulin') || lower.includes('amoxicilina')) {
        reply = {
          isUser: false,
          text: "Análise Clínica: Amoxicilina + Clavulanato de Potássio (Referência: Clavulin BD).",
          technical: "CLASSE: Antibacteriano Betalactâmico + Inibidor suicida de beta-lactamase. RECEITUÁRIO: Receita de Controle Especial em 2 Vias (RDC 20/2011).",
          mechanism: "A amoxicilina inibe a síntese da parede celular bacteriana. O clavulanato inativa irreversivelmente as beta-lactamases, restaurando o espectro contra estirpes produtoras.",
          alerts: [
            "Receita de Controle Especial em 2 vias obrigatória. Validade de 10 dias corridos a partir da emissão",
            "Administrar preferencialmente no início das refeições para minimizar intolerância gastrointestinal e diarreia",
            "Suspensões orais reconstituídas devem ser guardadas sob refrigeração (2°C a 8°C) por até 7 a 10 dias"
          ],
          citations: ["RDC ANVISA 20/2011", "Bula Oficial GSK Clavulin BD", "Diretrizes Brasileiras de Pneumonia"]
        };
      } else if (lower.includes('fluoxetina') && lower.includes('tramadol')) {
        reply = {
          isUser: false,
          text: "ALERTA GRAVE: A associação entre Fluoxetina e Tramadol apresenta alto risco de Síndrome Serotoninérgica potencialmente fatal e rebaixamento do limiar convulsivo.",
          technical: "INTERAÇÃO GRAVE (CYP2D6 & SEROTONINA): Fluoxetina é potente inibidora da CYP2D6 e bloqueadora da recaptação de serotonina.",
          mechanism: "O tramadol depende da enzima CYP2D6 para ser bioativado em seu metabólito analgésico O-desmetiltramadol (M1). A inibição reduz o alívio da dor e acumula o composto precursor serotoninérgico.",
          alerts: [
            "Monitorar sinais de toxicidade: hipertermia, hiperreflexia, agitação, clônus e sudorese intensa",
            "Conduta farmacêutica: sugerir ao médico substituição do analgésico (ex: paracetamol, dipirona) ou suspensão orientada"
          ],
          citations: ["ANVISA Farmacovigilância", "FDA Black Box Warnings", "Micromedex Interaction Checker"]
        };
      } else if (lower.includes('rivotril') || lower.includes('clonazepam')) {
        reply = {
          isUser: false,
          text: "Análise Regulatória e Clínica: Clonazepam (Referência: Rivotril).",
          technical: "CLASSE: Benzodiazepínico psicotrópico (Lista B1 - Portaria 344/98). Exige NOTIFICAÇÃO DE RECEITA B (AZUL).",
          mechanism: "Potencializa a ação inibitória do ácido gama-aminobutírico (GABA) no receptor GABA-A, promovendo sedação, ansiólise e efeito anticonvulsivante.",
          alerts: [
            "Notificação de Receita B (Azul) com retenção compulsória e escrituração no SNGPC. Validade de 30 dias",
            "Alerta de tolerância e dependência física com uso prolongado superior a 4 a 6 semanas",
            "Contraindicado associar com bebidas alcoólicas e opioides (risco de parada respiratória)"
          ],
          citations: ["Portaria SVS/MS 344/1998", "Bula Padrão Roche Rivotril"]
        };
      } else if (lower.includes('losartana')) {
        reply = {
          isUser: false,
          text: "Análise Clínica: Losartana Potássica (Referência: Cozaar).",
          technical: "CLASSE: Bloqueador do Receptor de Angiotensina II (BRA). DISPENSAÇÃO: Receita Branca Simples (1 via).",
          mechanism: "Bloqueia seletivamente os receptores AT1 da angiotensina II, impedindo vasoconstrição e secreção de aldosterona, reduzindo a resistência vascular periférica.",
          alerts: [
            "Interação frequente de balcão: Não associar com Anti-inflamatórios (AINEs como Ibuprofeno), pois cortam o efeito e sobrecarregam os rins",
            "Contraindicado na gestação (Categoria D)",
            "Aferir PA regularmente e monitorar potássio sérico (risco de hipercalemia em nefropatas)"
          ],
          citations: ["Diretrizes Brasileiras de Hipertensão SBC 2020", "Bulário ANVISA"]
        };
      } else if (lower.includes('glifage') || lower.includes('metformina')) {
        reply = {
          isUser: false,
          text: "Análise Clínica: Cloridrato de Metformina (Referência: Glifage / Glifage XR).",
          technical: "CLASSE: Biguanida antidiabética. DISPENSAÇÃO: Receita Branca Simples (1 via).",
          mechanism: "Reduz a produção hepática de glicose (gliconeogênese), diminui a absorção intestinal e melhora a sensibilidade periférica à insulina.",
          alerts: [
            "A formulação XR (liberação prolongada) NÃO deve ser partida ou triturada",
            "Tomar sempre durante ou após a principal refeição (jantar) para minimizar náuseas e diarreia",
            "Contraindicada em pacientes com Clearance de Creatinina < 30 mL/min por risco de acidose láctica"
          ],
          citations: ["Diretrizes SBD 2023-2024", "ANVISA Bula Glifage XR"]
        };
      } else if (lower.includes('ciprofloxacino')) {
        reply = {
          isUser: false,
          text: "O uso de Ciprofloxacino em idosos exige ajuste rigoroso pelo Clearance de Creatinina e monitoramento do tendão de Aquiles.",
          technical: "ALERTA GERIÁTRICO & AJUSTE RENAL: Fluoroquinolona. Receita de Controle Especial em 2 Vias (RDC 20/2011).",
          mechanism: "Inibe as enzimas bacterianas DNA-girase e topoisomerase IV. Pode quelar cátions divalentes (cálcio, ferro, antiácidos) se administrados juntos.",
          alerts: [
            "Se ClCr 30-50 mL/min: administrar a cada 12 horas; se < 30 mL/min: administrar a cada 18 ou 24 horas",
            "Evitar leite, derivados e suplementos de ferro/cálcio nas 2 horas antes ou depois da tomada",
            "Risco aumentado de tendinite e ruptura de tendão, potencializado por corticoides"
          ],
          citations: ["Sanford Guide 2024", "Bula ANVISA Ciprofloxacino", "RDC 20/2011"]
        };
      } else if (lower.includes('dipirona') || lower.includes('paracetamol')) {
        reply = {
          isUser: false,
          text: "Comparativo Clínico de Balcão: Dipirona vs Paracetamol.",
          technical: "Ambos são Medicamentos Isentos de Prescrição (MIP / Venda Livre) com posologias distintas.",
          mechanism: "Dipirona atua central e perifericamente inibindo a COX e canais de potássio, com efeito analgésico e espasmolítico. Paracetamol atua prioritariamente no SNC.",
          alerts: [
            "Paracetamol: dose máxima estrita de 4.000 mg/dia em adultos (risco de hepatotoxicidade aguda por acúmulo de NAPQI)",
            "Dipirona: contraindicada em pacientes com deficiência de G6PD ou histórico de reação idiossincrática a pirazolonas"
          ],
          citations: ["Farmacopeia Brasileira", "Conselho Federal de Farmácia (CFF)"]
        };
      } else {
        reply = {
          isUser: false,
          text: `Análise Clínica Farmacêutica para: "${text}".`,
          technical: `Consulta processada pelo motor farmacológico NEXFAR em conformidade com as RDCs da ANVISA.`,
          mechanism: `Avaliação do mecanismo de ação celular, meia-vida de eliminação plasmática e sítios de metabolização hepática CYP450.`,
          alerts: [
            "Conferir a posologia prescrita com a faixa terapêutica padrão para idade e peso",
            "Certificar-se do tipo oficial de receita (Branca 1 via, Branca 2 vias, Azul B ou Amarela A)",
            "Recomendar adesão rigorosa aos horários e nunca interromper sem avaliação profissional"
          ],
          citations: ["ANVISA - Bulário Eletrônico", "Farmacopeia Brasileira 6ª Ed."]
        };
      }

      setMessages(prev => [...prev, reply]);
      setIsThinking(false);
    }, 600);
  };

  const getPrescriptionBadge = (type: PrescriptionType) => {
    switch (type) {
      case 'ISENTO_MIP':
        return { label: 'MIP • Venda Livre', bg: 'bg-emerald-100 text-emerald-800 border-emerald-300' };
      case 'BRANCA_SIMPLES':
        return { label: 'Receita Branca (1 via)', bg: 'bg-slate-100 text-slate-800 border-slate-300' };
      case 'BRANCA_2VIAS_ANTIMICROBIANO':
        return { label: 'Receita 2 Vias (RDC 20/2011)', bg: 'bg-blue-100 text-blue-800 border-blue-300' };
      case 'BRANCA_2VIAS_C1':
        return { label: 'Receita 2 Vias C1 (Portaria 344)', bg: 'bg-indigo-100 text-indigo-800 border-indigo-300' };
      case 'NOTIFICACAO_B_AZUL':
        return { label: 'Notificação B (Azul)', bg: 'bg-sky-100 text-sky-900 border-sky-400 font-extrabold' };
      case 'NOTIFICACAO_A_AMARELA':
        return { label: 'Notificação A (Amarela)', bg: 'bg-amber-100 text-amber-900 border-amber-400 font-extrabold' };
      default:
        return { label: 'Prescrição Médica', bg: 'bg-gray-100 text-gray-800 border-gray-300' };
    }
  };

  const filteredMedicines = medicines.filter(m => {
    const matchesSearch =
      m.tradeName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      m.activeIngredient.toLowerCase().includes(searchQuery.toLowerCase()) ||
      m.manufacturer.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesTarja = tarjaFilter === 'ALL' || m.tarja === tarjaFilter;
    const matchesPrescription = prescriptionFilter === 'ALL' || m.prescriptionType === prescriptionFilter;

    return matchesSearch && matchesTarja && matchesPrescription;
  });

  const filteredInterchangeable = interchangeableDrugs.filter(d => {
    const q = interchangeableSearch.toLowerCase();
    return (
      d.activeIngredient.toLowerCase().includes(q) ||
      d.referenceDrug.toLowerCase().includes(q) ||
      d.genericOptions.some(g => g.toLowerCase().includes(q)) ||
      d.similarEquivalents.some(s => s.toLowerCase().includes(q)) ||
      d.therapeuticClass.toLowerCase().includes(q)
    );
  });

  const handleApproveAccount = (accId: string) => {
    setAccessAccounts(prev =>
      prev.map(acc => acc.id === accId ? { ...acc, status: 'ATIVO' } : acc)
    );
    showToast("Acesso do usuário aprovado com sucesso!");
  };

  const handleToggleBlockAccount = (accId: string) => {
    setAccessAccounts(prev =>
      prev.map(acc => {
        if (acc.id === accId) {
          const nextStatus = acc.status === 'BLOQUEADO' ? 'ATIVO' : 'BLOQUEADO';
          return { ...acc, status: nextStatus };
        }
        return acc;
      })
    );
    showToast("Status da conta atualizado!");
  };

  const handleChangePlanTier = (accId: string, newTier: 'INDIVIDUAL' | 'PROFISSIONAL' | 'ESTABELECIMENTO') => {
    setAccessAccounts(prev =>
      prev.map(acc => acc.id === accId ? { ...acc, planTier: newTier } : acc)
    );
    showToast(`Plano alterado para ${newTier}!`);
  };

  return (
    <div className="min-h-screen flex flex-col bg-[#F4F7FA]">
      {/* Toast Notification */}
      {toastMessage && (
        <div className="fixed bottom-6 right-6 z-50 bg-[#142D43] text-white text-sm px-4 py-3 rounded-xl shadow-xl border border-[#20A89A] flex items-center gap-2 animate-bounce">
          <CheckCircle2 className="w-4 h-4 text-[#20A89A]" />
          <span>{toastMessage}</span>
        </div>
      )}

      {/* Top Banner Disclaimer */}
      <header className="bg-[#142D43] text-white text-xs py-2 px-4 border-b border-[#1C3E5C]">
        <div className="max-w-7xl mx-auto flex flex-wrap justify-between items-center gap-2">
          <div className="flex items-center gap-2">
            <Shield className="w-4 h-4 text-[#20A89A]" />
            <span>NEXFAR SaaS • Apoio Clínico & Regulação ANVISA (RDC 20/2011, Portaria 344/98, Lei 9.787/99)</span>
          </div>
          <div className="flex items-center gap-4">
            <span className="hidden sm:inline text-gray-300">
              Ambiente: <strong className="text-white">{currentOrg.name}</strong>
            </span>
            <button
              onClick={() => {
                setIsAdminMode(true);
                setCurrentTab('admin');
              }}
              className="text-[#20A89A] hover:underline font-semibold flex items-center gap-1 bg-[#1C3E5C] px-2.5 py-0.5 rounded"
            >
              <Lock className="w-3 h-3" /> Painel Master SaaS
            </button>
          </div>
        </div>
      </header>

      {/* Main App Navigation Bar */}
      <nav className="bg-white border-b border-[#E2E8F0] sticky top-0 z-40 shadow-xs">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 flex justify-between items-center h-16">
          {/* Logo & Brand */}
          <div className="flex items-center gap-3">
            <div
              onClick={() => { setIsAdminMode(false); setCurrentTab('dashboard'); }}
              className="flex items-center gap-2.5 cursor-pointer"
            >
              <div className="w-10 h-10 rounded-xl bg-gradient-to-tr from-[#142D43] to-[#20A89A] flex items-center justify-center text-white shadow-md shadow-[#20A89A]/20">
                <Pill className="w-6 h-6" />
              </div>
              <div>
                <span className="font-extrabold text-xl tracking-tight text-[#142D43]">NEXFAR</span>
                <span className="text-[10px] block font-semibold text-[#20A89A] leading-none uppercase tracking-wider">
                  Inteligência Farmacêutica
                </span>
              </div>
            </div>

            {/* Org Switcher */}
            <div className="hidden md:flex items-center ml-6 border-l border-gray-200 pl-4">
              <Building2 className="w-4 h-4 text-gray-400 mr-1.5" />
              <select
                value={currentOrg.id}
                onChange={(e) => {
                  const sel = organizations.find(o => o.id === e.target.value);
                  if (sel) {
                    setCurrentOrg(sel);
                    if (sel.id === 'org_admin') {
                      setIsAdminMode(true);
                      setCurrentTab('admin');
                    }
                  }
                }}
                className="text-xs font-medium text-gray-700 bg-transparent border-none focus:ring-0 cursor-pointer"
              >
                {organizations.map(org => (
                  <option key={org.id} value={org.id}>{org.name}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Navigation Links */}
          <div className="hidden lg:flex items-center gap-1 text-sm font-medium">
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('dashboard'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'dashboard' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <LayoutDashboard className="w-4 h-4" /> Início
            </button>
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('medicines'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'medicines' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <Search className="w-4 h-4" /> Bulário & Intercambialidade
            </button>
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('assistant'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'assistant' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <Bot className="w-4 h-4 text-[#20A89A]" /> Assistente IA
            </button>
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('calculators'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'calculators' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <Calculator className="w-4 h-4" /> Cálculos
            </button>
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('patients'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'patients' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <Users className="w-4 h-4" /> Pacientes
            </button>
            <button
              onClick={() => { setIsAdminMode(false); setCurrentTab('plans'); }}
              className={`px-3 py-2 rounded-lg transition-all flex items-center gap-1.5 ${
                currentTab === 'plans' && !isAdminMode
                  ? 'bg-[#E6F7F5] text-[#178377] font-bold'
                  : 'text-gray-600 hover:text-gray-900 hover:bg-gray-100'
              }`}
            >
              <Layers className="w-4 h-4" /> Planos
            </button>
          </div>

          {/* Right Header: Dynamic User Profile & Logout */}
          <div className="flex items-center gap-3">
            <div
              onClick={() => setShowUserModal(true)}
              className="flex items-center gap-2 cursor-pointer bg-gray-50 hover:bg-gray-100 p-1.5 rounded-xl border border-gray-200 transition-all"
              title="Clique para editar dados ou trocar de conta"
            >
              <div className="w-8 h-8 rounded-lg bg-[#142D43] text-white flex items-center justify-center font-bold text-xs">
                {currentUser.name.slice(0, 2).toUpperCase()}
              </div>
              <div className="hidden sm:block text-left pr-2">
                <div className="text-xs font-bold text-[#142D43] leading-none">{currentUser.name}</div>
                <div className="text-[10px] text-gray-500 mt-0.5">{currentUser.crf} • {currentUser.role}</div>
              </div>
            </div>

            <button
              onClick={() => setShowUserModal(true)}
              className="p-2 text-gray-400 hover:text-red-600 hover:bg-red-50 rounded-lg transition-colors"
              title="Sair ou Trocar Conta"
            >
              <LogOut className="w-4 h-4" />
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content Body */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
        {isAdminMode || currentTab === 'admin' ? (
          /* ======================================================== */
          /* TAB 1: PAINEL MASTER SAAS (GERENCIAMENTO DE ACESSOS)     */
          /* ======================================================== */
          <div className="space-y-6">
            <div className="bg-gradient-to-r from-[#142D43] to-[#1C3E5C] text-white p-6 rounded-2xl shadow-md">
              <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                <div>
                  <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full bg-[#20A89A]/20 text-[#20A89A] text-xs font-bold mb-2">
                    <Shield className="w-3.5 h-3.5" /> SUPER ADMIN SAAS • CONTROLE TOTAL
                  </div>
                  <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight">
                    Painel Master NEXFAR SaaS
                  </h1>
                  <p className="text-sm text-gray-300 mt-1">
                    Gerencie quem tem autorização para acessar o aplicativo e configure os planos contratados.
                  </p>
                </div>

                <div className="flex flex-wrap gap-2">
                  <button
                    onClick={() => copyToClipboard("https://speedy-opal-3medrmg.tmp.now", "Link do Painel Master")}
                    className="bg-[#20A89A] hover:bg-[#178377] text-white font-bold text-xs px-3.5 py-2.5 rounded-xl transition-all flex items-center gap-1.5 shadow"
                  >
                    <Copy className="w-3.5 h-3.5" /> Copiar Link do Painel
                  </button>
                  <button
                    onClick={() => { setIsAdminMode(false); setCurrentTab('dashboard'); }}
                    className="bg-white/10 hover:bg-white/20 text-white font-semibold text-xs px-3.5 py-2.5 rounded-xl transition-all flex items-center gap-1.5"
                  >
                    Voltar ao App
                  </button>
                </div>
              </div>

              {/* Master Link Banner */}
              <div className="mt-4 p-3 bg-white/10 rounded-xl border border-white/20 flex flex-wrap items-center justify-between gap-3 text-xs">
                <div className="flex items-center gap-2">
                  <ExternalLink className="w-4 h-4 text-[#20A89A]" />
                  <span>URL Oficial da Nuvem: <strong className="text-white underline">https://speedy-opal-3medrmg.tmp.now</strong></span>
                </div>
                <span className="text-gray-300">Acesse de qualquer navegador desktop ou mobile para gerenciar farmácias parceiras.</span>
              </div>
            </div>

            {/* Sub-tabs */}
            <div className="flex gap-2 border-b border-gray-200 pb-2">
              <button
                onClick={() => setAdminSubTab('accesses')}
                className={`px-4 py-2 text-sm font-bold rounded-lg transition-all flex items-center gap-2 ${
                  adminSubTab === 'accesses'
                    ? 'bg-[#142D43] text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-100 border'
                }`}
              >
                <Users className="w-4 h-4" /> Acessos & Planos de Usuários ({accessAccounts.length})
              </button>
              <button
                onClick={() => setAdminSubTab('metrics')}
                className={`px-4 py-2 text-sm font-bold rounded-lg transition-all flex items-center gap-2 ${
                  adminSubTab === 'metrics'
                    ? 'bg-[#142D43] text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-100 border'
                }`}
              >
                <TrendingUp className="w-4 h-4" /> Métricas Financeiras
              </button>
              <button
                onClick={() => setAdminSubTab('audit')}
                className={`px-4 py-2 text-sm font-bold rounded-lg transition-all flex items-center gap-2 ${
                  adminSubTab === 'audit'
                    ? 'bg-[#142D43] text-white'
                    : 'bg-white text-gray-600 hover:bg-gray-100 border'
                }`}
              >
                <Activity className="w-4 h-4" /> Auditoria & SNGPC
              </button>
            </div>

            {/* SubTab Content: Accesses */}
            {adminSubTab === 'accesses' && (
              <div className="bg-white rounded-2xl border border-[#E2E8F0] shadow-sm p-6 space-y-4">
                <div className="flex justify-between items-center">
                  <div>
                    <h2 className="text-lg font-bold text-[#142D43]">Controle de Contas & Liberação de Acesso</h2>
                    <p className="text-xs text-gray-500">Defina se o farmacêutico ou drogaria pode usar o aplicativo e selecione o plano atribuído.</p>
                  </div>
                  <button
                    onClick={() => {
                      const newAcc: UserAccessAccount = {
                        id: `acc_${Date.now()}`,
                        userName: "Novo Profissional",
                        email: "novo@farmacia.com.br",
                        crfNumber: "CRF Pendente",
                        pharmacyName: "Farmácia Cadastrada",
                        cnpj: "00.000.000/0001-00",
                        planTier: "INDIVIDUAL",
                        status: "ATIVO",
                        dateRegistered: new Date().toLocaleDateString('pt-BR')
                      };
                      setAccessAccounts([newAcc, ...accessAccounts]);
                      showToast("Novo acesso adicionado!");
                    }}
                    className="bg-[#142D43] hover:bg-[#20A89A] text-white text-xs font-bold px-3 py-2 rounded-xl flex items-center gap-1.5 transition-all"
                  >
                    <Plus className="w-4 h-4" /> Adicionar Acesso
                  </button>
                </div>

                <div className="overflow-x-auto">
                  <table className="w-full text-left border-collapse text-sm">
                    <thead>
                      <tr className="border-b border-gray-200 text-xs font-bold text-gray-500 uppercase bg-gray-50/50">
                        <th className="py-3 px-4">Profissional / Drogaria</th>
                        <th className="py-3 px-4">Registro / CNPJ</th>
                        <th className="py-3 px-4">Plano Atribuído</th>
                        <th className="py-3 px-4">Status</th>
                        <th className="py-3 px-4 text-right">Ações de Gestão</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-100">
                      {accessAccounts.map(acc => (
                        <tr key={acc.id} className="hover:bg-gray-50/70 transition-colors">
                          <td className="py-3.5 px-4">
                            <div className="font-bold text-[#142D43]">{acc.userName}</div>
                            <div className="text-xs text-gray-500">{acc.email} • {acc.pharmacyName}</div>
                          </td>
                          <td className="py-3.5 px-4 text-xs font-medium text-gray-600">
                            <div>{acc.crfNumber}</div>
                            <div className="text-[11px] text-gray-400">{acc.cnpj}</div>
                          </td>
                          <td className="py-3.5 px-4">
                            <select
                              value={acc.planTier}
                              onChange={(e) => handleChangePlanTier(acc.id, e.target.value as any)}
                              className="text-xs font-bold px-2 py-1 rounded bg-slate-100 border border-gray-300 text-slate-800"
                            >
                              <option value="INDIVIDUAL">INDIVIDUAL (R$ 89/mês)</option>
                              <option value="PROFISSIONAL">PROFISSIONAL (R$ 179/mês)</option>
                              <option value="ESTABELECIMENTO">ESTABELECIMENTO (R$ 389/mês)</option>
                            </select>
                          </td>
                          <td className="py-3.5 px-4">
                            <span className={`text-[10px] font-extrabold px-2.5 py-1 rounded-full border ${
                              acc.status === 'ATIVO'
                                ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                                : acc.status === 'PENDENTE'
                                ? 'bg-amber-50 text-amber-700 border-amber-200'
                                : 'bg-red-50 text-red-700 border-red-200'
                            }`}>
                              {acc.status}
                            </span>
                          </td>
                          <td className="py-3.5 px-4 text-right space-x-1.5">
                            {acc.status === 'PENDENTE' && (
                              <button
                                onClick={() => handleApproveAccount(acc.id)}
                                className="text-xs bg-emerald-600 hover:bg-emerald-700 text-white font-bold px-2.5 py-1 rounded-lg"
                              >
                                Aprovar
                              </button>
                            )}
                            <button
                              onClick={() => handleToggleBlockAccount(acc.id)}
                              className={`text-xs font-semibold px-2.5 py-1 rounded-lg border ${
                                acc.status === 'BLOQUEADO'
                                  ? 'bg-blue-50 text-blue-700 border-blue-200'
                                  : 'bg-red-50 text-red-700 border-red-200 hover:bg-red-100'
                              }`}
                            >
                              {acc.status === 'BLOQUEADO' ? 'Desbloquear' : 'Bloquear'}
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            )}

            {/* SubTab Metrics */}
            {adminSubTab === 'metrics' && (
              <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                <div className="bg-white p-5 rounded-2xl border border-gray-200 shadow-sm">
                  <div className="text-xs font-bold text-gray-500 uppercase">Receita Recorrente (MRR)</div>
                  <div className="text-2xl font-black text-[#142D43] mt-2">R$ 24.890,00</div>
                  <div className="text-xs text-emerald-600 font-semibold mt-1">↑ +14.2% neste mês</div>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-gray-200 shadow-sm">
                  <div className="text-xs font-bold text-gray-500 uppercase">Assinaturas Ativas</div>
                  <div className="text-2xl font-black text-[#142D43] mt-2">128 Farmácias</div>
                  <div className="text-xs text-emerald-600 font-semibold mt-1">99.4% Taxa de Retenção</div>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-gray-200 shadow-sm">
                  <div className="text-xs font-bold text-gray-500 uppercase">Consultas no Bulário</div>
                  <div className="text-2xl font-black text-[#142D43] mt-2">18.420 / semana</div>
                  <div className="text-xs text-[#20A89A] font-semibold mt-1">Top: Clavulin & Rivotril</div>
                </div>
                <div className="bg-white p-5 rounded-2xl border border-gray-200 shadow-sm">
                  <div className="text-xs font-bold text-gray-500 uppercase">Conformidade ANVISA</div>
                  <div className="text-2xl font-black text-[#142D43] mt-2">100% SNGPC</div>
                  <div className="text-xs text-gray-500 mt-1">RDC 20/2011 e Portaria 344</div>
                </div>
              </div>
            )}

            {/* SubTab Audit */}
            {adminSubTab === 'audit' && (
              <div className="bg-white rounded-2xl border border-gray-200 shadow-sm p-6">
                <h3 className="font-bold text-[#142D43] mb-4">Trilha de Auditoria e Conformidade</h3>
                <div className="divide-y divide-gray-100 text-sm">
                  {[
                    { id: 1, action: "Alteração de Plano para Estabelecimento", actor: "Gestão Master", tenant: "Drogarias FarmaVida", time: "Há 10 min", status: "SUCESSO" },
                    { id: 2, action: "Validação de Receita Antimicrobiano RDC 20/2011", actor: "Farmacêutico RT", tenant: "Farmácia Matriz", time: "Há 25 min", status: "SUCESSO" },
                    { id: 3, action: "Novo Acesso de Balconista Cadastrado", actor: "Administrador", tenant: "Drogaria Popular Central", time: "Há 1 hora", status: "SUCESSO" }
                  ].map(log => (
                    <div key={log.id} className="py-3 flex justify-between items-center">
                      <div>
                        <div className="font-semibold text-gray-800">{log.action}</div>
                        <div className="text-xs text-gray-500">{log.actor} • {log.tenant}</div>
                      </div>
                      <div className="flex items-center gap-3">
                        <span className="text-xs text-gray-400">{log.time}</span>
                        <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-emerald-50 text-emerald-700 border border-emerald-200">
                          {log.status}
                        </span>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        ) : currentTab === 'dashboard' ? (
          /* ======================================================== */
          /* TAB 2: DASHBOARD INICIAL                                 */
          /* ======================================================== */
          <div className="space-y-6">
            {/* Welcome Banner */}
            <div className="bg-gradient-to-r from-[#142D43] via-[#1A3A56] to-[#20A89A] text-white p-6 sm:p-8 rounded-2xl shadow-sm relative overflow-hidden">
              <div className="relative z-10 max-w-2xl">
                <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full bg-white/10 backdrop-blur text-xs font-semibold text-[#E6F7F5] mb-3">
                  <Sparkles className="w-3.5 h-3.5 text-[#20A89A]" />
                  <span>Plataforma Farmacêutica Inteligente Ativa</span>
                </div>
                <h1 className="text-2xl sm:text-4xl font-extrabold tracking-tight">
                  Bem-vindo, {currentUser.name}
                </h1>
                <p className="mt-2 text-sm sm:text-base text-gray-200">
                  {currentUser.role} ({currentUser.crf}) • Unidade: <strong className="text-white underline">{currentOrg.name}</strong>. Ambiente pronto para consulta de bulas, tabela de intercambialidade e checagem de receitas.
                </p>

                <div className="mt-6 flex flex-wrap gap-3">
                  <button
                    onClick={() => setCurrentTab('assistant')}
                    className="bg-[#20A89A] hover:bg-[#178377] text-white font-bold text-sm px-5 py-2.5 rounded-xl shadow-lg shadow-[#20A89A]/30 transition-all flex items-center gap-2"
                  >
                    <Bot className="w-4 h-4" /> Consultar Assistente IA
                  </button>
                  <button
                    onClick={() => {
                      setBularioView('interchangeable');
                      setCurrentTab('medicines');
                    }}
                    className="bg-white/10 hover:bg-white/20 text-white font-semibold text-sm px-5 py-2.5 rounded-xl backdrop-blur transition-all flex items-center gap-2"
                  >
                    <ArrowRightLeft className="w-4 h-4 text-[#20A89A]" /> Tabela Ético x Genérico x Similar
                  </button>
                  <button
                    onClick={() => setShowUserModal(true)}
                    className="bg-white/10 hover:bg-white/20 text-white font-semibold text-sm px-4 py-2.5 rounded-xl backdrop-blur transition-all flex items-center gap-1.5"
                  >
                    <User className="w-4 h-4" /> Meus Dados Reais
                  </button>
                </div>
              </div>
            </div>

            {/* Quick Cards Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              <div
                onClick={() => { setBularioView('interchangeable'); setCurrentTab('medicines'); }}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-teal-50 text-[#20A89A] flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <ArrowRightLeft className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-base text-[#142D43]">Intercambialidade</h3>
                <p className="text-xs text-gray-500 mt-1">Guia comparativo Ético, Genérico e Similar (EQ) para balconistas.</p>
                <span className="text-[11px] font-bold text-[#20A89A] mt-3 inline-block">Acessar Guia de Balcão →</span>
              </div>

              <div
                onClick={() => { setBularioView('catalog'); setCurrentTab('medicines'); }}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Pill className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-base text-[#142D43]">Bulário Oficial</h3>
                <p className="text-xs text-gray-500 mt-1">Consulta com tarja regulatória e tipo de receita necessária (RDC 20, Portaria 344).</p>
                <span className="text-[11px] font-bold text-blue-600 mt-3 inline-block">Consultar Bulas →</span>
              </div>

              <div
                onClick={() => setCurrentTab('assistant')}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Bot className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-base text-[#142D43]">Assistente Clínico IA</h3>
                <p className="text-xs text-gray-500 mt-1">Respostas imediatas sobre dosagens, interações e farmacocinética.</p>
                <span className="text-[11px] font-bold text-purple-600 mt-3 inline-block">Abrir Chat Farmacêutico →</span>
              </div>

              <div
                onClick={() => { setIsAdminMode(true); setCurrentTab('admin'); }}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-amber-50 text-amber-700 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Lock className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-base text-[#142D43]">Painel Master SaaS</h3>
                <p className="text-xs text-gray-500 mt-1">Gerencie os acessos de usuários e liberação de planos contratados.</p>
                <span className="text-[11px] font-bold text-amber-700 mt-3 inline-block">Gerenciar Acessos →</span>
              </div>
            </div>

            {/* Quick Preview Section */}
            <div className="bg-white rounded-2xl border border-[#E2E8F0] p-6 shadow-sm">
              <div className="flex justify-between items-center mb-4">
                <div>
                  <h3 className="font-bold text-[#142D43] text-lg">Medicamentos com Tipos de Receita Regulamentares</h3>
                  <p className="text-xs text-gray-500">Padrão nacional ANVISA para liberação no balcão da farmácia.</p>
                </div>
                <button
                  onClick={() => { setBularioView('catalog'); setCurrentTab('medicines'); }}
                  className="text-xs font-bold text-[#20A89A] hover:underline"
                >
                  Ver todos no Bulário ({medicines.length})
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {medicines.slice(0, 3).map(med => {
                  const badge = getPrescriptionBadge(med.prescriptionType);
                  return (
                    <div
                      key={med.id}
                      onClick={() => { setSelectedMedicine(med); setCurrentTab('medicines'); }}
                      className="p-4 rounded-xl border border-gray-100 hover:border-[#20A89A] hover:bg-[#F8FAFC] cursor-pointer transition-all flex flex-col justify-between"
                    >
                      <div>
                        <div className="flex justify-between items-start gap-2">
                          <span className="font-bold text-sm text-[#142D43] line-clamp-1">{med.tradeName}</span>
                          <span className={`text-[10px] font-bold px-2 py-0.5 rounded border ${badge.bg}`}>
                            {badge.label}
                          </span>
                        </div>
                        <div className="text-xs text-gray-500 mt-1 font-medium">{med.activeIngredient}</div>
                        <div className="text-[11px] text-gray-400 mt-0.5">{med.manufacturer}</div>
                      </div>
                      <div className="mt-4 pt-3 border-t border-gray-100 flex justify-between items-center text-xs">
                        <span className="text-gray-500">Tarja:</span>
                        <span className="font-bold text-gray-800">{med.tarja.replace('_', ' ')}</span>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        ) : currentTab === 'assistant' ? (
          /* ======================================================== */
          /* TAB 3: ASSISTENTE IA CLÍNICO                             */
          /* ======================================================== */
          <div className="bg-white rounded-2xl border border-[#E2E8F0] shadow-sm flex flex-col h-[75vh]">
            {/* Header */}
            <div className="p-4 border-b border-[#E2E8F0] flex items-center justify-between bg-gradient-to-r from-white to-[#F8FAFC]">
              <div className="flex items-center gap-3">
                <div className="w-10 h-10 rounded-xl bg-[#E6F7F5] text-[#20A89A] flex items-center justify-center">
                  <Bot className="w-6 h-6" />
                </div>
                <div>
                  <h2 className="font-bold text-[#142D43]">Assistente Clínico & Regulatório Farmacêutico IA</h2>
                  <p className="text-xs text-gray-500">Motor Farmacológico com RDCs ANVISA, prescrições e interações</p>
                </div>
              </div>
              <button
                onClick={() => setMessages([messages[0]])}
                className="text-xs font-semibold text-gray-500 hover:text-[#142D43] px-3 py-1.5 rounded-lg border hover:bg-gray-50"
              >
                Nova Conversa
              </button>
            </div>

            {/* Prompt Chips */}
            <div className="p-3 bg-[#F8FAFC] border-b border-gray-100 flex gap-2 overflow-x-auto text-xs">
              {[
                "Regras de dispensação do Clavulin (RDC 20/2011)",
                "Interação grave entre Fluoxetina e Tramadol",
                "Receituário necessário para Rivotril (Clonazepam)",
                "Ajuste de dosagem de Metformina no paciente idoso",
                "Comparativo prático entre Dipirona e Paracetamol"
              ].map((chip, idx) => (
                <button
                  key={idx}
                  onClick={() => handleSendChat(chip)}
                  className="whitespace-nowrap bg-white border border-gray-200 hover:border-[#20A89A] hover:text-[#20A89A] px-3 py-1.5 rounded-full font-medium text-gray-700 shadow-2xs transition-all"
                >
                  ⚡ {chip}
                </button>
              ))}
            </div>

            {/* Chat Messages */}
            <div className="flex-1 overflow-y-auto p-4 sm:p-6 space-y-4">
              {messages.map((msg, i) => (
                <div key={i} className={`flex ${msg.isUser ? 'justify-end' : 'justify-start'}`}>
                  <div className={`max-w-2xl rounded-2xl p-4 ${
                    msg.isUser
                      ? 'bg-[#142D43] text-white rounded-br-xs'
                      : 'bg-[#F8FAFC] border border-[#E2E8F0] text-gray-800 rounded-bl-xs'
                  }`}>
                    {!msg.isUser && (
                      <div className="flex items-center justify-between mb-2 pb-2 border-b border-gray-200">
                        <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-[#E6F7F5] text-[#178377] uppercase tracking-wide">
                          Análise Farmacêutica Validada
                        </span>
                        <button
                          onClick={() => copyToClipboard(msg.text + "\n\n" + (msg.mechanism || ''), "Resposta clínica")}
                          className="text-gray-400 hover:text-gray-600"
                          title="Copiar texto"
                        >
                          <Copy className="w-4 h-4" />
                        </button>
                      </div>
                    )}
                    <p className="text-sm font-semibold">{msg.text}</p>
                    {msg.technical && (
                      <div className="mt-2 text-xs text-gray-600 bg-white p-2.5 rounded-lg border border-gray-100">
                        <strong>Parecer Técnico:</strong> {msg.technical}
                      </div>
                    )}
                    {msg.mechanism && (
                      <div className="mt-2 text-xs text-gray-600 bg-white p-2.5 rounded-lg border border-gray-100">
                        <strong>Mecanismo de Ação / Farmacocinética:</strong> {msg.mechanism}
                      </div>
                    )}
                    {msg.alerts && msg.alerts.length > 0 && (
                      <div className="mt-2 space-y-1">
                        {msg.alerts.map((al, idx) => (
                          <div key={idx} className="text-xs text-amber-800 bg-amber-50 p-2 rounded border border-amber-200 flex items-start gap-1.5">
                            <AlertTriangle className="w-3.5 h-3.5 shrink-0 mt-0.5 text-amber-600" />
                            <span>{al}</span>
                          </div>
                        ))}
                      </div>
                    )}
                    {msg.citations && (
                      <div className="mt-3 pt-2 border-t border-gray-200/60 flex flex-wrap gap-1">
                        {msg.citations.map((c, idx) => (
                          <span key={idx} className="text-[10px] bg-gray-200/60 px-2 py-0.5 rounded text-gray-600">
                            📚 {c}
                          </span>
                        ))}
                      </div>
                    )}
                  </div>
                </div>
              ))}

              {isThinking && (
                <div className="flex items-center gap-2 text-xs text-gray-500 bg-white border border-gray-200 p-3 rounded-xl w-fit">
                  <div className="w-4 h-4 border-2 border-[#20A89A] border-t-transparent rounded-full animate-spin"></div>
                  <span>Processando literatura farmacêutica e diretrizes regulatórias...</span>
                </div>
              )}
            </div>

            {/* Input Bar */}
            <div className="p-3 sm:p-4 border-t border-[#E2E8F0] bg-white">
              <form
                onSubmit={(e) => { e.preventDefault(); handleSendChat(); }}
                className="flex items-center gap-2"
              >
                <input
                  type="text"
                  value={chatInput}
                  onChange={(e) => setChatInput(e.target.value)}
                  placeholder="Digite qualquer medicamento, princípio ativo, dosagem ou tipo de receita..."
                  className="flex-1 bg-[#F4F7FA] border border-gray-300 rounded-xl px-4 py-2.5 text-sm focus:outline-none focus:ring-2 focus:ring-[#20A89A]"
                />
                <button
                  type="submit"
                  disabled={!chatInput.trim()}
                  className="bg-[#142D43] hover:bg-[#20A89A] text-white p-2.5 rounded-xl transition-all disabled:opacity-40"
                >
                  <Send className="w-5 h-5" />
                </button>
              </form>
            </div>
          </div>
        ) : currentTab === 'medicines' ? (
          /* ======================================================== */
          /* TAB 4: BULÁRIO ANVISA & TABELA DE INTERCAMBIALIDADE      */
          /* ======================================================== */
          <div className="space-y-6">
            {/* Header with View Switcher */}
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 bg-white p-5 rounded-2xl border border-gray-200 shadow-sm">
              <div>
                <h1 className="text-2xl font-black text-[#142D43]">Consulta Farmacêutica & Bulário</h1>
                <p className="text-xs sm:text-sm text-gray-500">
                  {bularioView === 'catalog'
                    ? "Bulário oficial com classificação de tarja e padrão de receituário brasileiro."
                    : "Tabela comparativa Ético x Genérico x Similar para treinamento prático de balconistas."}
                </p>
              </div>

              {/* View Switcher Buttons */}
              <div className="flex bg-gray-100 p-1 rounded-xl">
                <button
                  onClick={() => setBularioView('catalog')}
                  className={`text-xs px-3.5 py-2 rounded-lg font-bold transition-all flex items-center gap-1.5 ${
                    bularioView === 'catalog'
                      ? 'bg-white text-[#142D43] shadow-sm'
                      : 'text-gray-600 hover:text-gray-900'
                  }`}
                >
                  <Pill className="w-3.5 h-3.5" /> Bulário Oficial
                </button>
                <button
                  onClick={() => setBularioView('interchangeable')}
                  className={`text-xs px-3.5 py-2 rounded-lg font-bold transition-all flex items-center gap-1.5 ${
                    bularioView === 'interchangeable'
                      ? 'bg-[#20A89A] text-white shadow-sm'
                      : 'text-gray-600 hover:text-gray-900'
                  }`}
                >
                  <ArrowRightLeft className="w-3.5 h-3.5" /> Tabela Ético x Genérico x Similar
                </button>
              </div>
            </div>

            {/* VIEW 1: CATALOG VIEW */}
            {bularioView === 'catalog' && (
              <div className="space-y-4">
                {/* Search & Filters */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-3">
                  <div className="md:col-span-2 relative">
                    <Search className="w-5 h-5 text-gray-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                    <input
                      type="text"
                      value={searchQuery}
                      onChange={(e) => setSearchQuery(e.target.value)}
                      placeholder="Buscar por medicamento, princípio ativo ou fabricante (ex: Clavulin, Losartana)..."
                      className="w-full pl-11 pr-4 py-3 bg-white border border-[#E2E8F0] rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#20A89A] shadow-xs"
                    />
                  </div>

                  {/* Filter Dropdown */}
                  <div>
                    <select
                      value={prescriptionFilter}
                      onChange={(e) => setPrescriptionFilter(e.target.value)}
                      className="w-full py-3 px-3 bg-white border border-[#E2E8F0] rounded-xl text-xs font-bold text-gray-700 shadow-xs"
                    >
                      <option value="ALL">Todos os Tipos de Receita</option>
                      <option value="ISENTO_MIP">MIP (Medicamento Isento)</option>
                      <option value="BRANCA_SIMPLES">Receita Branca Simples (1 via)</option>
                      <option value="BRANCA_2VIAS_ANTIMICROBIANO">Receita 2 Vias (Antimicrobianos - RDC 20)</option>
                      <option value="NOTIFICACAO_B_AZUL">Notificação B (Azul - Psicotrópicos)</option>
                      <option value="NOTIFICACAO_A_AMARELA">Notificação A (Amarela - Entorpecentes)</option>
                    </select>
                  </div>
                </div>

                {/* Medicines Cards */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                  {filteredMedicines.map(med => {
                    const badge = getPrescriptionBadge(med.prescriptionType);
                    return (
                      <div
                        key={med.id}
                        onClick={() => setSelectedMedicine(med)}
                        className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:border-[#20A89A] hover:shadow-md transition-all cursor-pointer flex flex-col justify-between"
                      >
                        <div>
                          <div className="flex justify-between items-start gap-2">
                            <h3 className="font-bold text-base text-[#142D43]">{med.tradeName}</h3>
                            <span className={`text-[10px] font-extrabold px-2 py-0.5 rounded border ${badge.bg}`}>
                              {badge.label}
                            </span>
                          </div>

                          <div className="text-xs font-semibold text-[#20A89A] mt-1">{med.activeIngredient}</div>
                          <div className="text-xs text-gray-500 mt-0.5">{med.dosageForm} • {med.manufacturer}</div>

                          {/* Reference & Generic labels */}
                          {med.referenceBrand && (
                            <div className="mt-3 text-[11px] bg-gray-50 p-2 rounded-lg border border-gray-100 space-y-1">
                              <div><strong className="text-gray-700">Ético:</strong> {med.referenceBrand}</div>
                              {med.genericName && <div><strong className="text-gray-700">Genérico:</strong> {med.genericName}</div>}
                            </div>
                          )}

                          <div className="mt-3 text-xs text-gray-600 line-clamp-2">
                            <strong>Indicações:</strong> {med.approvedIndications.join(', ')}
                          </div>
                        </div>

                        <div className="mt-4 pt-3 border-t border-gray-100 flex items-center justify-between text-xs">
                          <span className="text-gray-500">PMC: <strong>R$ {med.pmcPrice.toFixed(2)}</strong></span>
                          <span className="text-[#20A89A] font-bold flex items-center gap-0.5">
                            Ver Bula Completa <ChevronRight className="w-3.5 h-3.5" />
                          </span>
                        </div>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}

            {/* VIEW 2: INTERCHANGEABILITY COMPARATIVE TABLE (ÉTICO X GENÉRICO X SIMILAR) */}
            {bularioView === 'interchangeable' && (
              <div className="space-y-4">
                {/* Search Bar for Interchangeability */}
                <div className="relative">
                  <Search className="w-5 h-5 text-gray-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
                  <input
                    type="text"
                    value={interchangeableSearch}
                    onChange={(e) => setInterchangeableSearch(e.target.value)}
                    placeholder="Pesquisar por princípio ativo, nome de referência, genérico ou similar (ex: Clavulin, Rivotril, Losartana, Novalgina)..."
                    className="w-full pl-11 pr-4 py-3 bg-white border border-[#E2E8F0] rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#20A89A] shadow-xs"
                  />
                </div>

                {/* Educational Banner for Clerks */}
                <div className="bg-gradient-to-r from-[#E6F7F5] to-blue-50 border border-[#20A89A]/30 p-4 rounded-xl flex items-start gap-3">
                  <GraduationCap className="w-5 h-5 text-[#20A89A] shrink-0 mt-0.5" />
                  <div className="text-xs text-gray-700">
                    <strong className="text-[#142D43] block text-sm mb-1">Guia de Intercambialidade para o Balcão da Farmácia (Lei 9.787/99 e RDC 58/2014)</strong>
                    O medicamento <strong>Genérico</strong> pode ser intercambiável com o de <strong>Referência (Ético)</strong>. O <strong>Similar Equivalente (EQ)</strong> constante na lista da ANVISA pode substituir o de Referência. Verifique sempre o tipo de receita exigido antes da dispensação.
                  </div>
                </div>

                {/* Comparative Table / Cards */}
                <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
                  <div className="overflow-x-auto">
                    <table className="w-full text-left border-collapse text-sm">
                      <thead>
                        <tr className="bg-[#142D43] text-white text-xs font-bold uppercase">
                          <th className="py-3.5 px-4 w-1/4">Princípio Ativo & Dosagem</th>
                          <th className="py-3.5 px-4 w-1/4 bg-[#1C3E5C]">1. Ético (Referência)</th>
                          <th className="py-3.5 px-4 w-1/4">2. Genéricos Registrados</th>
                          <th className="py-3.5 px-4 w-1/4">3. Similares Equivalentes (EQ)</th>
                        </tr>
                      </thead>
                      <tbody className="divide-y divide-gray-200">
                        {filteredInterchangeable.map(drug => {
                          const badge = getPrescriptionBadge(drug.prescriptionType);
                          return (
                            <React.Fragment key={drug.id}>
                              <tr className="hover:bg-gray-50/70 transition-colors">
                                {/* Princípio Ativo */}
                                <td className="py-4 px-4 align-top">
                                  <div className="font-extrabold text-[#142D43] text-sm">{drug.activeIngredient}</div>
                                  <div className="text-xs font-semibold text-[#20A89A] mt-0.5">{drug.dosage}</div>
                                  <div className="text-[11px] text-gray-500 mt-1">{drug.therapeuticClass}</div>
                                  <div className="mt-2">
                                    <span className={`text-[10px] font-bold px-2 py-0.5 rounded border inline-block ${badge.bg}`}>
                                      {badge.label}
                                    </span>
                                  </div>
                                </td>

                                {/* Coluna 1: Ético */}
                                <td className="py-4 px-4 align-top bg-blue-50/30 border-l border-r border-blue-100">
                                  <div className="inline-block px-2 py-0.5 rounded text-[10px] font-extrabold bg-[#142D43] text-white uppercase tracking-wider mb-1">
                                    REFERÊNCIA
                                  </div>
                                  <div className="font-extrabold text-[#142D43] text-base">{drug.referenceDrug}</div>
                                  <div className="text-xs text-gray-500">{drug.referenceLab}</div>
                                </td>

                                {/* Coluna 2: Genérico */}
                                <td className="py-4 px-4 align-top">
                                  <div className="inline-block px-2 py-0.5 rounded text-[10px] font-extrabold bg-amber-500 text-white uppercase tracking-wider mb-1">
                                    GENÉRICO (G)
                                  </div>
                                  <div className="font-semibold text-gray-800 text-xs">
                                    {drug.activeIngredient} (Genérico)
                                  </div>
                                  <div className="text-[11px] text-gray-500 mt-1 space-y-0.5">
                                    {drug.genericOptions.map((gen, idx) => (
                                      <div key={idx}>• {gen}</div>
                                    ))}
                                  </div>
                                </td>

                                {/* Coluna 3: Similar EQ */}
                                <td className="py-4 px-4 align-top">
                                  <div className="inline-block px-2 py-0.5 rounded text-[10px] font-extrabold bg-teal-600 text-white uppercase tracking-wider mb-1">
                                    SIMILAR EQ
                                  </div>
                                  <div className="text-xs font-semibold text-gray-800 space-y-1">
                                    {drug.similarEquivalents.map((sim, idx) => (
                                      <div key={idx} className="bg-gray-50 px-2 py-1 rounded border border-gray-100">
                                        ★ {sim}
                                      </div>
                                    ))}
                                  </div>
                                </td>
                              </tr>

                              {/* Dica para o Balconista Row */}
                              <tr className="bg-amber-50/40 border-b-2 border-gray-200">
                                <td colSpan={4} className="py-2.5 px-4 text-xs text-amber-900">
                                  <div className="flex items-start gap-2">
                                    <Sparkles className="w-3.5 h-3.5 text-amber-600 shrink-0 mt-0.5" />
                                    <span>
                                      <strong>Dica de Balcão para o Balconista:</strong> {drug.clerkTip} — <em>{drug.prescriptionLabel}</em>
                                    </span>
                                  </div>
                                </td>
                              </tr>
                            </React.Fragment>
                          );
                        })}
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            )}
          </div>
        ) : currentTab === 'calculators' ? (
          /* ======================================================== */
          /* TAB 5: CÁLCULOS FARMACÊUTICOS                            */
          /* ======================================================== */
          <div className="space-y-6">
            <h1 className="text-2xl font-black text-[#142D43]">Calculadoras Clínicas & Diluições</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              {/* Infusão / Gotas */}
              <div className="bg-white p-6 rounded-2xl border border-gray-200 shadow-sm space-y-4">
                <div className="flex items-center gap-2">
                  <Calculator className="w-5 h-5 text-[#20A89A]" />
                  <h3 className="font-bold text-base text-[#142D43]">Gotejamento de Infusão (Gotas/min)</h3>
                </div>
                <div className="grid grid-cols-2 gap-3 text-xs">
                  <div>
                    <label className="text-gray-600 font-semibold">Volume (mL)</label>
                    <input
                      type="number"
                      value={volDrops}
                      onChange={(e) => setVolDrops(e.target.value)}
                      className="w-full mt-1 p-2 border rounded-lg text-sm"
                    />
                  </div>
                  <div>
                    <label className="text-gray-600 font-semibold">Tempo (Horas)</label>
                    <input
                      type="number"
                      value={timeDrops}
                      onChange={(e) => setTimeDrops(e.target.value)}
                      className="w-full mt-1 p-2 border rounded-lg text-sm"
                    />
                  </div>
                </div>
                <div className="bg-[#E6F7F5] p-4 rounded-xl text-center">
                  <div className="text-xs text-[#178377] font-semibold">Velocidade Calculada</div>
                  <div className="text-3xl font-black text-[#142D43] mt-1">
                    {Math.round((Number(volDrops) || 500) / ((Number(timeDrops) || 8) * 3))} <span className="text-base font-medium">gotas/min</span>
                  </div>
                  <div className="text-[11px] text-gray-500 mt-1">
                    Equivalente a {(Math.round((Number(volDrops) || 500) / ((Number(timeDrops) || 8) * 3))) * 3} microgotas/min
                  </div>
                </div>
              </div>

              {/* Clearance de Creatinina (Cockcroft-Gault) */}
              <div className="bg-white p-6 rounded-2xl border border-gray-200 shadow-sm space-y-4">
                <div className="flex items-center gap-2">
                  <Stethoscope className="w-5 h-5 text-blue-600" />
                  <h3 className="font-bold text-base text-[#142D43]">Clearance de Creatinina (Cockcroft-Gault)</h3>
                </div>
                <div className="grid grid-cols-3 gap-2 text-xs">
                  <div>
                    <label className="text-gray-600 font-semibold">Idade</label>
                    <input
                      type="number"
                      value={creatAge}
                      onChange={(e) => setCreatAge(e.target.value)}
                      className="w-full mt-1 p-2 border rounded-lg text-sm"
                    />
                  </div>
                  <div>
                    <label className="text-gray-600 font-semibold">Peso (kg)</label>
                    <input
                      type="number"
                      value={creatWeight}
                      onChange={(e) => setCreatWeight(e.target.value)}
                      className="w-full mt-1 p-2 border rounded-lg text-sm"
                    />
                  </div>
                  <div>
                    <label className="text-gray-600 font-semibold">Cr Sérica</label>
                    <input
                      type="number"
                      step="0.1"
                      value={creatSerum}
                      onChange={(e) => setCreatSerum(e.target.value)}
                      className="w-full mt-1 p-2 border rounded-lg text-sm"
                    />
                  </div>
                </div>
                <div className="bg-blue-50 p-4 rounded-xl text-center">
                  <div className="text-xs text-blue-800 font-semibold">Taxa de Filtração Estimada</div>
                  <div className="text-3xl font-black text-[#142D43] mt-1">
                    {Math.round(((140 - (Number(creatAge) || 60)) * (Number(creatWeight) || 70)) / (72 * (Number(creatSerum) || 1.0)) * (creatIsFemale ? 0.85 : 1.0))}
                    <span className="text-base font-medium"> mL/min</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : currentTab === 'patients' ? (
          /* ======================================================== */
          /* TAB 6: GESTÃO DE PACIENTES                               */
          /* ======================================================== */
          <div className="space-y-6">
            <div className="flex justify-between items-center">
              <div>
                <h1 className="text-2xl font-black text-[#142D43]">Acompanhamento Farmacoterapêutico</h1>
                <p className="text-xs text-gray-500">Registro de pacientes, aferições e adesão medicamentosa.</p>
              </div>
              <button
                onClick={() => setShowAddPatient(true)}
                className="bg-[#142D43] hover:bg-[#20A89A] text-white text-xs font-bold px-3.5 py-2.5 rounded-xl flex items-center gap-1.5 transition-all"
              >
                <Plus className="w-4 h-4" /> Cadastrar Paciente
              </button>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {patientList.map(pat => (
                <div key={pat.id} className="bg-white p-5 rounded-2xl border border-gray-200 shadow-sm space-y-3">
                  <div className="flex justify-between items-start">
                    <div>
                      <h4 className="font-bold text-[#142D43]">{pat.name}</h4>
                      <span className="text-xs text-gray-500">{pat.age} anos • {pat.lastVisitDate}</span>
                    </div>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-emerald-100 text-emerald-800">
                      {pat.statusBadge}
                    </span>
                  </div>

                  <div className="grid grid-cols-2 gap-2 text-xs bg-gray-50 p-2.5 rounded-xl border border-gray-100">
                    <div>PA: <strong>{pat.systolicBP}/{pat.diastolicBP} mmHg</strong></div>
                    <div>Glicemia: <strong>{pat.bloodGlucose} mg/dL</strong></div>
                    <div>Peso: <strong>{pat.weightKg} kg</strong></div>
                    <div>Altura: <strong>{pat.heightCm} cm</strong></div>
                  </div>

                  <div className="text-xs text-gray-600">
                    <strong>Medicamentos em Uso:</strong> {pat.continuousMedicines.join(', ')}
                  </div>
                </div>
              ))}
            </div>
          </div>
        ) : (
          /* ======================================================== */
          /* TAB 7: PLANOS & ASSINATURA                               */
          /* ======================================================== */
          <div className="space-y-6">
            <div className="text-center max-w-xl mx-auto">
              <h1 className="text-3xl font-black text-[#142D43]">Planos Comerciais NEXFAR</h1>
              <p className="text-sm text-gray-500 mt-2">
                Soluções completas para farmacêuticos autônomos, drogarias e redes com prescrições digitais e inteligência clínica.
              </p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-5xl mx-auto">
              {[
                { name: "INDIVIDUAL", price: "R$ 89", period: "/mês", desc: "Para farmacêutico clínico autônomo e consultório", badge: "Iniciante", feat: ["Bulário ANVISA completo", "50 pacientes ativos", "Assistente IA Padrão", "Calculadoras Clínicas"] },
                { name: "PROFISSIONAL", price: "R$ 179", period: "/mês", desc: "Para farmácias independentes e drogarias", badge: "Mais Popular", feat: ["Intercambialidade Ético/Genérico/Similar", "Pacientes ilimitados", "Assistente IA Avançado (RAG)", "Suporte Técnico Prioritário"] },
                { name: "ESTABELECIMENTO", price: "R$ 389", period: "/mês", desc: "Para redes de farmácias e franquias", badge: "Corporativo", feat: ["Multi-unidades e filiais", "Painel Master SaaS", "Sincronização com ERP de Balcão", "Treinamento para balconistas"] }
              ].map((pl, idx) => (
                <div key={idx} className={`bg-white rounded-2xl p-6 border shadow-sm flex flex-col justify-between ${idx === 1 ? 'border-2 border-[#20A89A] shadow-md relative' : 'border-gray-200'}`}>
                  {idx === 1 && (
                    <span className="absolute -top-3 left-1/2 -translate-x-1/2 bg-[#20A89A] text-white text-[10px] font-extrabold px-3 py-1 rounded-full uppercase tracking-wider">
                      Recomendado
                    </span>
                  )}
                  <div>
                    <h3 className="font-extrabold text-lg text-[#142D43]">{pl.name}</h3>
                    <p className="text-xs text-gray-500 mt-1">{pl.desc}</p>
                    <div className="mt-4 flex items-baseline">
                      <span className="text-3xl font-black text-[#142D43]">{pl.price}</span>
                      <span className="text-xs text-gray-500 ml-1">{pl.period}</span>
                    </div>
                    <ul className="mt-6 space-y-2.5 text-xs text-gray-600">
                      {pl.feat.map((f, i) => (
                        <li key={i} className="flex items-center gap-2">
                          <Check className="w-4 h-4 text-[#20A89A]" /> {f}
                        </li>
                      ))}
                    </ul>
                  </div>
                  <button
                    onClick={() => {
                      setIsAdminMode(true);
                      setCurrentTab('admin');
                    }}
                    className={`mt-6 w-full py-2.5 rounded-xl font-bold text-xs transition-all ${
                      idx === 1 ? 'bg-[#20A89A] text-white hover:bg-[#178377]' : 'bg-[#142D43] text-white hover:bg-[#20A89A]'
                    }`}
                  >
                    Gerenciar no Painel Master
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      {/* Detail Modal for Selected Medicine */}
      {selectedMedicine && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-2xl w-full max-h-[85vh] overflow-y-auto p-6 space-y-4 shadow-2xl">
            <div className="flex justify-between items-start border-b border-gray-100 pb-3">
              <div>
                <h3 className="text-xl font-bold text-[#142D43]">{selectedMedicine.tradeName}</h3>
                <p className="text-xs text-[#20A89A] font-semibold">{selectedMedicine.activeIngredient} ({selectedMedicine.concentration})</p>
              </div>
              <button onClick={() => setSelectedMedicine(null)} className="text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            {/* Badges */}
            <div className="flex flex-wrap gap-2 text-xs">
              <span className={`px-2.5 py-1 rounded-full font-bold border ${getPrescriptionBadge(selectedMedicine.prescriptionType).bg}`}>
                {getPrescriptionBadge(selectedMedicine.prescriptionType).label}
              </span>
              <span className="px-2.5 py-1 rounded-full font-bold bg-slate-100 text-slate-800">
                {selectedMedicine.dosageForm}
              </span>
              <span className="px-2.5 py-1 rounded-full font-bold bg-emerald-50 text-emerald-800">
                PMC: R$ {selectedMedicine.pmcPrice.toFixed(2)}
              </span>
            </div>

            {/* Details Grid */}
            <div className="text-xs space-y-3 bg-gray-50 p-4 rounded-xl">
              <div><strong>Fabricante:</strong> {selectedMedicine.manufacturer}</div>
              <div><strong>Registro ANVISA:</strong> {selectedMedicine.anvisaRegistry}</div>
              <div><strong>Regras de Receita:</strong> {selectedMedicine.prescriptionDescription}</div>
              {selectedMedicine.referenceBrand && (
                <div><strong>Marca de Referência (Ético):</strong> {selectedMedicine.referenceBrand}</div>
              )}
              {selectedMedicine.genericName && (
                <div><strong>Opções Genéricas:</strong> {selectedMedicine.genericName}</div>
              )}
              {selectedMedicine.similarBrands && (
                <div><strong>Similares Equivalentes (EQ):</strong> {selectedMedicine.similarBrands}</div>
              )}
            </div>

            <div className="text-xs space-y-2">
              <h4 className="font-bold text-[#142D43]">Indicações em Bula:</h4>
              <ul className="list-disc pl-5 space-y-1 text-gray-700">
                {selectedMedicine.approvedIndications.map((ind, i) => <li key={i}>{ind}</li>)}
              </ul>
            </div>

            <div className="text-xs space-y-2">
              <h4 className="font-bold text-[#142D43]">Posologia Aprovada:</h4>
              <p className="text-gray-700 bg-blue-50 p-3 rounded-lg border border-blue-100">{selectedMedicine.posologyApproved}</p>
            </div>

            <div className="text-xs space-y-2">
              <h4 className="font-bold text-red-700">Contraindicações:</h4>
              <ul className="list-disc pl-5 space-y-1 text-red-600">
                {selectedMedicine.contraindications.map((c, i) => <li key={i}>{c}</li>)}
              </ul>
            </div>

            <div className="flex justify-end pt-2 border-t border-gray-100">
              <button
                onClick={() => setSelectedMedicine(null)}
                className="bg-[#142D43] text-white text-xs font-bold px-4 py-2 rounded-xl"
              >
                Fechar
              </button>
            </div>
          </div>
        </div>
      )}

      {/* User Modal: Switch Account / Clean Onboarding */}
      {showUserModal && (
        <div className="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4">
          <div className="bg-white rounded-2xl max-w-md w-full p-6 space-y-4 shadow-2xl">
            <div className="flex justify-between items-start border-b border-gray-100 pb-3">
              <div className="flex items-center gap-2">
                <User className="w-5 h-5 text-[#20A89A]" />
                <h3 className="font-bold text-[#142D43] text-lg">Perfil & Identificação Real</h3>
              </div>
              <button onClick={() => setShowUserModal(false)} className="text-gray-400 hover:text-gray-600">
                <X className="w-5 h-5" />
              </button>
            </div>

            <p className="text-xs text-gray-500">
              Personalize o aplicativo com seu nome real, CRF e drogaria para emissão de receituários e atendimento.
            </p>

            <div className="space-y-3 text-xs">
              <div>
                <label className="block font-bold text-gray-700 mb-1">Nome Completo</label>
                <input
                  type="text"
                  value={editUserName}
                  onChange={(e) => setEditUserName(e.target.value)}
                  placeholder="Ex: Dra. Juliana Santos"
                  className="w-full p-2.5 border rounded-lg text-sm"
                />
              </div>

              <div>
                <label className="block font-bold text-gray-700 mb-1">E-mail Profissional</label>
                <input
                  type="email"
                  value={editUserEmail}
                  onChange={(e) => setEditUserEmail(e.target.value)}
                  placeholder="exemplo@farmacia.com.br"
                  className="w-full p-2.5 border rounded-lg text-sm"
                />
              </div>

              <div>
                <label className="block font-bold text-gray-700 mb-1">Registro CRF / CRM</label>
                <input
                  type="text"
                  value={editUserCrf}
                  onChange={(e) => setEditUserCrf(e.target.value)}
                  placeholder="Ex: CRF-SP 54.321"
                  className="w-full p-2.5 border rounded-lg text-sm"
                />
              </div>

              <div>
                <label className="block font-bold text-gray-700 mb-1">Farmácia / Estabelecimento</label>
                <input
                  type="text"
                  value={editUserPharmacy}
                  onChange={(e) => setEditUserPharmacy(e.target.value)}
                  placeholder="Ex: Drogaria Nova Farma"
                  className="w-full p-2.5 border rounded-lg text-sm"
                />
              </div>

              <div>
                <label className="block font-bold text-gray-700 mb-1">Função / Cargo</label>
                <select
                  value={editUserRole}
                  onChange={(e) => setEditUserRole(e.target.value)}
                  className="w-full p-2.5 border rounded-lg text-sm font-medium"
                >
                  <option value="Farmacêutico RT">Farmacêutico RT</option>
                  <option value="Farmacêutico Clínico">Farmacêutico Clínico</option>
                  <option value="Balconista / Atendente">Balconista / Atendente</option>
                  <option value="Gestor de Drogaria">Gestor de Drogaria</option>
                </select>
              </div>
            </div>

            <div className="space-y-2 pt-2 border-t border-gray-100">
              <button
                onClick={() => {
                  setCurrentUser({
                    name: editUserName || "Farmacêutico(a)",
                    email: editUserEmail,
                    crf: editUserCrf,
                    pharmacy: editUserPharmacy,
                    role: editUserRole,
                    isLoggedIn: true
                  });
                  setShowUserModal(false);
                  showToast("Dados atualizados com sucesso!");
                }}
                className="w-full bg-[#142D43] hover:bg-[#20A89A] text-white font-bold py-2.5 rounded-xl text-xs transition-all"
              >
                Salvar Meus Dados Reais
              </button>

              <button
                onClick={() => {
                  setCurrentUser({
                    name: "Usuário Convidado",
                    email: "convidado@nexfar.com.br",
                    crf: "Modo Balcão",
                    pharmacy: "Farmácia Escola",
                    role: "Balconista / Atendente",
                    isLoggedIn: true
                  });
                  setShowUserModal(false);
                  showToast("Acessando como convidado");
                }}
                className="w-full bg-gray-100 hover:bg-gray-200 text-gray-700 font-semibold py-2 rounded-xl text-xs transition-all"
              >
                Deslogar e Entrar como Convidado
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
