import React, { useState } from 'react';
import {
  Activity,
  Bot,
  Calculator,
  Calendar,
  CheckCircle2,
  ChevronRight,
  ClipboardList,
  Copy,
  Database,
  ExternalLink,
  FileText,
  Filter,
  HeartPulse,
  HelpCircle,
  Layers,
  LayoutDashboard,
  LogOut,
  Pill,
  Plus,
  Search,
  Send,
  Shield,
  ShieldAlert,
  Sparkles,
  Stethoscope,
  TrendingUp,
  UserCheck,
  Users
} from 'lucide-react';
import { organizations, medicines, pathologies, mockPatients } from './data/mockData';
import { Medicine, Organization } from './types';
import { isSupabaseConfigured } from './lib/supabase';

type Tab = 'dashboard' | 'assistant' | 'medicines' | 'calculators' | 'patients' | 'pathologies' | 'plans' | 'admin';

export default function App() {
  const [currentTab, setCurrentTab] = useState<Tab>('dashboard');
  const [currentOrg, setCurrentOrg] = useState<Organization>(organizations[0]);
  const [isAdminMode, setIsAdminMode] = useState<boolean>(false);
  
  // Bulário search & filter
  const [searchQuery, setSearchQuery] = useState('');
  const [tarjaFilter, setTarjaFilter] = useState<string>('ALL');
  const [selectedMedicine, setSelectedMedicine] = useState<Medicine | null>(null);

  // Chat Assistant State
  const [messages, setMessages] = useState<Array<{ isUser: boolean; text: string; technical?: string; mechanism?: string; alerts?: string[]; citations?: string[] }>>([
    {
      isUser: false,
      text: "Olá, Dr. Walex! Sou o assistente clínico da NEXFAR. Como posso auxiliar na sua tomada de decisão farmacêutica hoje?",
      technical: "Motor clínico com checagem de interações, posologia e RDCs da ANVISA.",
      citations: ["ANVISA Bulário Eletrônico", "Farmacopeia Brasileira 6ª Ed.", "Micromedex Solutions"]
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

  const handleSendChat = (promptText?: string) => {
    const text = promptText || chatInput;
    if (!text.trim()) return;

    const userMsg = { isUser: true, text };
    setMessages(prev => [...prev, userMsg]);
    if (!promptText) setChatInput('');
    setIsThinking(true);

    setTimeout(() => {
      let reply = {
        isUser: false,
        text: `Avaliação clínica para "${text}". Conforme as diretrizes oficiais da ANVISA e literatura clínica de referência.`,
        technical: "Triagem técnica de suporte à conduta farmacêutica e dispensação orientada.",
        mechanism: "A farmacoterapia requer avaliação individualizada de posologia, função renal/hepática e adesão do paciente ao tratamento contínuo.",
        alerts: [
          "Verificar receituário oficial com carimbo, CRM e validade regulamentar",
          "Orientar o paciente sobre horários das tomadas com ou sem alimentos"
        ],
        citations: ["ANVISA - Bulário Eletrônico", "Diretrizes Clínicas Brasileiras"]
      };

      const lower = text.toLowerCase();
      if (lower.includes('fluoxetina') && lower.includes('tramadol')) {
        reply = {
          isUser: false,
          text: "A associação entre Fluoxetina e Tramadol apresenta risco clinicamente significativo de Síndrome Serotoninérgica grave e redução do limiar convulsivo.",
          technical: "INTERAÇÃO GRAVE: Fluoxetina inibe potentemente a isoenzima CYP2D6 e eleva a transmissão serotoninérgica no SNC.",
          mechanism: "O tramadol é um pró-fármaco ativado pela CYP2D6 para o metabólito M1. A fluoxetina reduz seu efeito analgésico e sinergiza o bloqueio de recaptação de serotonina.",
          alerts: [
            "Alerta de Síndrome Serotoninérgica: hipertermia, clônus, tremor, agitação",
            "Conduta: evitar associação ou trocar analgésico sob supervisão médica"
          ],
          citations: ["ANVISA Farmacovigilância", "FDA Black Box Warnings", "Lexicomp"]
        };
      } else if (lower.includes('ciprofloxacino')) {
        reply = {
          isUser: false,
          text: "O uso de Ciprofloxacino em idosos exige ajuste rigoroso pelo Clearance de Creatinina e monitoramento do tendão de Aquiles.",
          technical: "ALERTA GERIÁTRICO & AJUSTE RENAL: Excreção renal predominante. Meia-vida prolongada em idosos com ClCr < 50 mL/min.",
          mechanism: "Quinolonas inibem DNA-girase bacteriana e podem quelar cátions divalentes no trato GI. Risco de ruptura tendínea ampliado com corticoides.",
          alerts: [
            "Se ClCr 30-50 mL/min: administrar a cada 12 horas",
            "Se ClCr < 30 mL/min: administrar a cada 18 ou 24 horas",
            "Manter hidratação adequada para prevenir cristalúria"
          ],
          citations: ["Sanford Guide 2024", "Bula ANVISA Ciprofloxacino"]
        };
      } else if (lower.includes('dipirona') || lower.includes('paracetamol')) {
        reply = {
          isUser: false,
          text: "Comparativo Clínico: Dipirona vs Paracetamol no Balcão Farmacêutico.",
          technical: "Dipirona tem ação analgésica, antipirética e espasmolítica. Paracetamol tem ação analgésica e antipirética no SNC sem ação periférica significativa.",
          mechanism: "Dipirona inibe COX e modula canais de potássio. Paracetamol inibe síntese de prostaglandinas centrais; primeira escolha na gestação (Cat B).",
          alerts: [
            "Paracetamol: dose máxima estrita de 4.000 mg/dia para prevenir hepatotoxicidade por NAPQI",
            "Dipirona: rara agranulocitose idiopática; contraindicada em porfiria e deficiência de G6PD"
          ],
          citations: ["Farmacopeia Brasileira", "Conselho Federal de Farmácia (CFF)"]
        };
      }

      setMessages(prev => [...prev, reply]);
      setIsThinking(false);
    }, 600);
  };

  const filteredMedicines = medicines.filter(m => {
    const matchesSearch =
      m.tradeName.toLowerCase().includes(searchQuery.toLowerCase()) ||
      m.activeIngredient.toLowerCase().includes(searchQuery.toLowerCase()) ||
      m.manufacturer.toLowerCase().includes(searchQuery.toLowerCase());
    
    if (tarjaFilter === 'ALL') return matchesSearch;
    return matchesSearch && m.tarja === tarjaFilter;
  });

  const handleAddPatient = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newPatientName) return;
    const [sys, dia] = newPatientBP.split('/').map(Number);
    const newP = {
      id: `pat_${Date.now()}`,
      name: newPatientName,
      age: Number(newPatientAge) || 45,
      systolicBP: sys || 120,
      diastolicBP: dia || 80,
      bloodGlucose: 95,
      weightKg: 70,
      heightCm: 170,
      continuousMedicines: ["Acompanhamento Inicial"],
      notes: "Novo paciente cadastrado para acompanhamento farmacoterapêutico.",
      lastVisitDate: new Date().toLocaleDateString('pt-BR'),
      statusBadge: (sys && sys > 140) ? "Atenção Pressórica" : "Parâmetros Estáveis"
    };
    setPatientList([newP, ...patientList]);
    setNewPatientName('');
    setNewPatientAge('');
    setNewPatientBP('');
    setShowAddPatient(false);
  };

  return (
    <div className="min-h-screen flex flex-col bg-[#F4F7FA]">
      {/* Top Banner Disclaimer */}
      <header className="bg-[#142D43] text-white text-xs py-2 px-4 border-b border-[#1C3E5C]">
        <div className="max-w-7xl mx-auto flex flex-wrap justify-between items-center gap-2">
          <div className="flex items-center gap-2">
            <Shield className="w-4 h-4 text-[#20A89A]" />
            <span>NEXFAR SaaS • Apoio à Decisão Farmacêutica e Regulação ANVISA (RDC 20/2011, Portaria 344/98)</span>
          </div>
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-1.5 bg-[#0D1E2D] px-2.5 py-1 rounded-full text-[11px]">
              <Database className="w-3.5 h-3.5 text-[#20A89A]" />
              <span>Supabase Cloud:</span>
              <span className={`inline-block w-2 h-2 rounded-full ${isSupabaseConfigured() ? 'bg-emerald-400' : 'bg-amber-400'}`}></span>
              <span className="text-gray-300">{isSupabaseConfigured() ? 'Conectado' : 'Pronto (Schema DDL)'}</span>
            </div>
            <span className="hidden sm:inline text-gray-400">Ambiente Seguro LGPD</span>
          </div>
        </div>
      </header>

      {/* Main Navigation Header */}
      <nav className="bg-white border-b border-[#E2E8F0] sticky top-0 z-40 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-between items-center h-16">
          {/* Logo & Tenant selector */}
          <div className="flex items-center gap-4">
            <div className="flex items-center gap-2.5 cursor-pointer" onClick={() => { setCurrentTab('dashboard'); setIsAdminMode(false); }}>
              <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-[#142D43] to-[#20A89A] flex items-center justify-center text-white shadow-md shadow-[#20A89A]/20">
                <HeartPulse className="w-5 h-5 text-white" />
              </div>
              <div>
                <span className="text-lg font-black tracking-tight text-[#142D43]">NEX<span className="text-[#20A89A]">FAR</span></span>
                <span className="hidden md:inline-block ml-2 text-[10px] font-bold px-2 py-0.5 bg-[#E6F7F5] text-[#178377] rounded">SAAS CLÍNICO</span>
              </div>
            </div>

            {/* Tenant switcher dropdown */}
            <div className="hidden lg:flex items-center">
              <select
                aria-label="Selecionar Estabelecimento Farmacêutico"
                value={currentOrg.id}
                onChange={(e) => {
                  const org = organizations.find(o => o.id === e.target.value);
                  if (org) {
                    setCurrentOrg(org);
                    if (org.id === 'org_admin') setIsAdminMode(true);
                  }
                }}
                className="text-xs bg-[#F4F7FA] border border-[#CBD5E1] rounded-lg px-3 py-1.5 font-medium text-[#142D43] focus:outline-none focus:ring-2 focus:ring-[#20A89A]"
              >
                {organizations.map(o => (
                  <option key={o.id} value={o.id}>{o.name}</option>
                ))}
              </select>
            </div>
          </div>

          {/* Navigation Links */}
          <div className="hidden md:flex items-center gap-1 text-sm font-semibold">
            {[
              { id: 'dashboard', label: 'Início', icon: LayoutDashboard },
              { id: 'assistant', label: 'Assistente IA', icon: Bot, badge: 'RAG' },
              { id: 'medicines', label: 'Bulário ANVISA', icon: Pill },
              { id: 'calculators', label: 'Calculadoras', icon: Calculator },
              { id: 'patients', label: 'Atenção Clínica', icon: ClipboardList },
              { id: 'pathologies', label: 'Mapas Clínicos', icon: Stethoscope },
              { id: 'plans', label: 'Planos', icon: Layers }
            ].map((tab) => {
              const Icon = tab.icon;
              const isActive = currentTab === tab.id && !isAdminMode;
              return (
                <button
                  key={tab.id}
                  onClick={() => { setCurrentTab(tab.id as Tab); setIsAdminMode(false); }}
                  className={`flex items-center gap-1.5 px-3 py-2 rounded-lg transition-all ${
                    isActive
                      ? 'bg-[#142D43] text-white shadow-sm'
                      : 'text-gray-600 hover:text-[#142D43] hover:bg-[#F4F7FA]'
                  }`}
                >
                  <Icon className="w-4 h-4" />
                  <span>{tab.label}</span>
                  {tab.badge && (
                    <span className="text-[9px] px-1.5 py-0.2 bg-[#20A89A] text-white rounded-full font-bold">
                      {tab.badge}
                    </span>
                  )}
                </button>
              );
            })}
          </div>

          {/* Right User & Mode actions */}
          <div className="flex items-center gap-3">
            <button
              onClick={() => setIsAdminMode(!isAdminMode)}
              className={`text-xs font-bold px-3 py-1.5 rounded-lg border transition-all ${
                isAdminMode
                  ? 'bg-amber-600 text-white border-amber-700'
                  : 'bg-white text-gray-700 border-gray-300 hover:border-[#142D43]'
              }`}
            >
              {isAdminMode ? 'Sair do Admin' : 'Painel SaaS'}
            </button>

            <div className="flex items-center gap-2 pl-2 border-l border-gray-200">
              <div className="w-8 h-8 rounded-full bg-[#142D43] text-white flex items-center justify-center font-bold text-xs">
                WF
              </div>
              <div className="hidden sm:block text-left">
                <div className="text-xs font-bold text-[#142D43] leading-none">Dr. Walex Ferreira</div>
                <div className="text-[10px] text-gray-500 font-medium">CRF-SP 48.912/RT</div>
              </div>
            </div>
          </div>
        </div>

        {/* Mobile secondary tab strip */}
        <div className="md:hidden flex overflow-x-auto px-4 py-2 gap-2 border-t border-gray-100 bg-[#F8FAFC]">
          {[
            { id: 'dashboard', label: 'Início' },
            { id: 'assistant', label: 'IA Clínica' },
            { id: 'medicines', label: 'Bulário' },
            { id: 'calculators', label: 'Cálculos' },
            { id: 'patients', label: 'Pacientes' },
            { id: 'pathologies', label: 'Mapas' },
            { id: 'plans', label: 'Planos' }
          ].map(t => (
            <button
              key={t.id}
              onClick={() => { setCurrentTab(t.id as Tab); setIsAdminMode(false); }}
              className={`whitespace-nowrap px-3 py-1 text-xs font-medium rounded-full ${
                currentTab === t.id && !isAdminMode ? 'bg-[#142D43] text-white' : 'bg-white text-gray-700 border'
              }`}
            >
              {t.label}
            </button>
          ))}
        </div>
      </nav>

      {/* Main App Body */}
      <main className="flex-1 max-w-7xl w-full mx-auto p-4 sm:p-6 lg:p-8">
        {/* ======================================================== */}
        {/* TAB 1: SAAS ADMIN PANEL                                  */}
        {/* ======================================================== */}
        {isAdminMode ? (
          <div className="space-y-6">
            <div className="bg-gradient-to-r from-[#142D43] to-[#1C3E5C] text-white p-6 rounded-2xl shadow-sm">
              <div className="flex justify-between items-start">
                <div>
                  <span className="text-xs font-bold tracking-wider text-[#20A89A] uppercase">SaaS Headquarters • Visão Executiva</span>
                  <h1 className="text-2xl sm:text-3xl font-black mt-1">Painel do Proprietário NEXFAR</h1>
                  <p className="text-sm text-gray-300 mt-1">Gestão de MRR, consumo de tokens de IA e clientes ativos em todo o Brasil.</p>
                </div>
                <button
                  onClick={() => setIsAdminMode(false)}
                  className="bg-white/10 hover:bg-white/20 text-white text-xs px-3 py-1.5 rounded-lg flex items-center gap-1"
                >
                  <LogOut className="w-3.5 h-3.5" /> Voltar ao Consultório
                </button>
              </div>

              <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 mt-6">
                <div className="bg-white/10 backdrop-blur rounded-xl p-4">
                  <div className="text-xs text-gray-300 font-medium">MRR (Receita Recorrente)</div>
                  <div className="text-2xl font-black text-emerald-400 mt-1">R$ 48.750,00</div>
                  <div className="text-[11px] text-emerald-300 mt-0.5">↑ +14.2% este mês</div>
                </div>
                <div className="bg-white/10 backdrop-blur rounded-xl p-4">
                  <div className="text-xs text-gray-300 font-medium">Farmácias / Redes</div>
                  <div className="text-2xl font-black text-white mt-1">382</div>
                  <div className="text-[11px] text-gray-300 mt-0.5">Contratos B2B ativos</div>
                </div>
                <div className="bg-white/10 backdrop-blur rounded-xl p-4">
                  <div className="text-xs text-gray-300 font-medium">Usuários Totais</div>
                  <div className="text-2xl font-black text-[#20A89A] mt-1">1.420</div>
                  <div className="text-[11px] text-gray-300 mt-0.5">890 ativos nas últimas 24h</div>
                </div>
                <div className="bg-white/10 backdrop-blur rounded-xl p-4">
                  <div className="text-xs text-gray-300 font-medium">Custo Operacional IA</div>
                  <div className="text-2xl font-black text-amber-300 mt-1">R$ 1.840,00</div>
                  <div className="text-[11px] text-gray-300 mt-0.5">Margem bruta: 96.2%</div>
                </div>
              </div>
            </div>

            {/* Audit Logs */}
            <div className="bg-white rounded-2xl p-6 border border-[#E2E8F0] shadow-sm">
              <h2 className="text-lg font-bold text-[#142D43] mb-4 flex items-center gap-2">
                <Activity className="w-5 h-5 text-[#20A89A]" />
                Logs de Auditoria e Conformidade SNGPC / ANVISA
              </h2>
              <div className="divide-y divide-gray-100">
                {[
                  { id: 1, action: "Alteração de Plano para Enterprise", actor: "Dr. Walex (Admin)", tenant: "Drogarias FarmaVida", time: "Há 12 min", status: "SUCCESS" },
                  { id: 2, action: "Escrituração de Receita RDC 20/2011", actor: "Farmacêutica RT - CRF 48.912", tenant: "Farmácia São Paulo", time: "Há 34 min", status: "SUCCESS" },
                  { id: 3, action: "Novo Paciente em Acompanhamento", actor: "Atendente Lucas R.", tenant: "Drogarias FarmaVida", time: "Há 1 hora", status: "SUCCESS" },
                  { id: 4, action: "Atualização Automática de Bula (Clavulin)", actor: "Nexfar Sync Bot", tenant: "Global ANVISA", time: "Há 3 horas", status: "SUCCESS" }
                ].map(log => (
                  <div key={log.id} className="py-3 flex flex-wrap items-center justify-between gap-2 text-sm">
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
                  Bem-vindo, Dr. Walex Ferreira
                </h1>
                <p className="mt-2 text-sm sm:text-base text-gray-200">
                  Unidade ativa: <strong className="text-white underline">{currentOrg.name}</strong> ({currentOrg.city}, {currentOrg.state}). Seu ambiente seguro para checagem clínica, bulas e acompanhamento de pacientes.
                </p>

                <div className="mt-6 flex flex-wrap gap-3">
                  <button
                    onClick={() => setCurrentTab('assistant')}
                    className="bg-[#20A89A] hover:bg-[#178377] text-white font-bold text-sm px-5 py-2.5 rounded-xl shadow-lg shadow-[#20A89A]/30 transition-all flex items-center gap-2"
                  >
                    <Bot className="w-4 h-4" /> Consultar Assistente IA
                  </button>
                  <button
                    onClick={() => setCurrentTab('medicines')}
                    className="bg-white/10 hover:bg-white/20 text-white font-semibold text-sm px-5 py-2.5 rounded-xl backdrop-blur transition-all flex items-center gap-2"
                  >
                    <Search className="w-4 h-4" /> Buscar no Bulário ANVISA
                  </button>
                </div>
              </div>
            </div>

            {/* Quick Cards Grid */}
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
              <div
                onClick={() => setCurrentTab('medicines')}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-blue-50 text-blue-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Pill className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-[#142D43]">Bulário & PMC ANVISA</h3>
                <p className="text-xs text-gray-500 mt-1">Consulte preços máximos, tarjas, retenção e posologias aprovadas.</p>
                <div className="mt-3 text-xs font-semibold text-[#20A89A] flex items-center gap-1">Acessar Bulário <ChevronRight className="w-3.5 h-3.5" /></div>
              </div>

              <div
                onClick={() => setCurrentTab('calculators')}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Calculator className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-[#142D43]">Calculadoras Clínicas</h3>
                <p className="text-xs text-gray-500 mt-1">Gotejamento, Cockcroft-Gault, diluições e doses pediátricas.</p>
                <div className="mt-3 text-xs font-semibold text-[#20A89A] flex items-center gap-1">Calcular agora <ChevronRight className="w-3.5 h-3.5" /></div>
              </div>

              <div
                onClick={() => setCurrentTab('patients')}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-purple-50 text-purple-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <ClipboardList className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-[#142D43]">Atenção Farmacêutica</h3>
                <p className="text-xs text-gray-500 mt-1">Registro de pacientes, glicemia capilar, PA e conciliação.</p>
                <div className="mt-3 text-xs font-semibold text-[#20A89A] flex items-center gap-1">Ver prontuários <ChevronRight className="w-3.5 h-3.5" /></div>
              </div>

              <div
                onClick={() => setCurrentTab('pathologies')}
                className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:shadow-md hover:border-[#20A89A] transition-all cursor-pointer group"
              >
                <div className="w-10 h-10 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center mb-3 group-hover:scale-110 transition-transform">
                  <Stethoscope className="w-5 h-5" />
                </div>
                <h3 className="font-bold text-[#142D43]">Mapas Clínicos & Red Flags</h3>
                <p className="text-xs text-gray-500 mt-1">Fluxogramas para HAS e DM2 com sinais de encaminhamento SAMU.</p>
                <div className="mt-3 text-xs font-semibold text-[#20A89A] flex items-center gap-1">Explorar mapas <ChevronRight className="w-3.5 h-3.5" /></div>
              </div>
            </div>

            {/* Featured Medicines Preview */}
            <div className="bg-white rounded-2xl p-6 border border-[#E2E8F0] shadow-sm">
              <div className="flex justify-between items-center mb-4">
                <div>
                  <h2 className="text-lg font-bold text-[#142D43]">Medicamentos Frequentes na Drogaria</h2>
                  <p className="text-xs text-gray-500">Dados oficiais ANVISA atualizados com PMC de São Paulo (ICMS 18%)</p>
                </div>
                <button
                  onClick={() => setCurrentTab('medicines')}
                  className="text-xs font-bold text-[#20A89A] hover:underline"
                >
                  Ver todos ({medicines.length})
                </button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {medicines.slice(0, 3).map(med => (
                  <div
                    key={med.id}
                    onClick={() => { setSelectedMedicine(med); setCurrentTab('medicines'); }}
                    className="p-4 rounded-xl border border-gray-100 hover:border-[#20A89A] hover:bg-[#F8FAFC] cursor-pointer transition-all flex flex-col justify-between"
                  >
                    <div>
                      <div className="flex justify-between items-start gap-2">
                        <span className="font-bold text-sm text-[#142D43] line-clamp-1">{med.tradeName}</span>
                        <span className={`text-[10px] font-bold px-2 py-0.5 rounded ${
                          med.tarja === 'VERMELHA_RETENCAO' ? 'bg-red-100 text-red-700' :
                          med.tarja === 'PRETA' ? 'bg-gray-900 text-white' :
                          med.tarja === 'LIVRE' ? 'bg-emerald-100 text-emerald-800' :
                          'bg-amber-100 text-amber-800'
                        }`}>
                          {med.tarja.replace('_', ' ')}
                        </span>
                      </div>
                      <div className="text-xs text-gray-500 mt-1 font-medium">{med.activeIngredient}</div>
                      <div className="text-[11px] text-gray-400 mt-0.5">{med.manufacturer}</div>
                    </div>
                    <div className="mt-4 pt-3 border-t border-gray-100 flex justify-between items-center text-xs">
                      <span className="text-gray-500">PMC Máximo:</span>
                      <span className="font-extrabold text-[#142D43]">R$ {med.pmcPrice.toFixed(2)}</span>
                    </div>
                  </div>
                ))}
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
                  <h2 className="font-bold text-[#142D43]">Assistente Farmacêutico IA</h2>
                  <p className="text-xs text-gray-500">Motor de Regras Clínicas & RAG sobre a Farmacopeia Brasileira</p>
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
                "Interação entre Fluoxetina e Tramadol",
                "Ajuste de Ciprofloxacino no Idoso",
                "Diferença entre Dipirona e Paracetamol"
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
                          Análise Clínica Verificada
                        </span>
                        <button
                          onClick={() => {
                            navigator.clipboard.writeText(msg.text + "\n\n" + (msg.mechanism || ''));
                            alert("Resposta copiada para o prontuário!");
                          }}
                          className="text-gray-400 hover:text-gray-600"
                          title="Copiar orientação"
                        >
                          <Copy className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    )}
                    <p className="text-sm font-medium leading-relaxed">{msg.text}</p>

                    {msg.technical && (
                      <div className="mt-3 p-3 bg-blue-50 border border-blue-100 rounded-xl text-xs text-blue-900 font-semibold">
                        {msg.technical}
                      </div>
                    )}

                    {msg.mechanism && (
                      <div className="mt-3 text-xs text-gray-700 leading-relaxed">
                        <strong className="text-gray-900 block mb-1">Mecanismo Farmacológico:</strong>
                        {msg.mechanism}
                      </div>
                    )}

                    {msg.alerts && msg.alerts.length > 0 && (
                      <div className="mt-3 space-y-1">
                        <strong className="text-xs text-red-600 block">Alertas de Segurança:</strong>
                        {msg.alerts.map((a, j) => (
                          <div key={j} className="text-xs text-gray-700 flex items-start gap-1.5">
                            <span className="text-red-500 font-bold">•</span>
                            <span>{a}</span>
                          </div>
                        ))}
                      </div>
                    )}

                    {msg.citations && msg.citations.length > 0 && (
                      <div className="mt-3 pt-2 border-t border-gray-200 text-[11px] text-gray-400">
                        Fontes: {msg.citations.join(' • ')}
                      </div>
                    )}
                  </div>
                </div>
              ))}

              {isThinking && (
                <div className="flex items-center gap-2 text-xs text-gray-500 bg-white border border-gray-200 p-3 rounded-xl w-fit">
                  <div className="w-4 h-4 border-2 border-[#20A89A] border-t-transparent rounded-full animate-spin"></div>
                  <span>Consultando bases de interações e monografias ANVISA...</span>
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
                  placeholder="Dúvida sobre dosagem, interação medicamentosa ou regulação..."
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
          /* TAB 4: BULÁRIO ANVISA                                    */
          /* ======================================================== */
          <div className="space-y-6">
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
              <div>
                <h1 className="text-2xl font-black text-[#142D43]">Consulta de Medicamentos & Bulas ANVISA</h1>
                <p className="text-xs sm:text-sm text-gray-500">Base oficial de medicamentos registrados, PMC e restrições de dispensação.</p>
              </div>

              {/* Tarja filter buttons */}
              <div className="flex gap-1.5 flex-wrap">
                {[
                  { id: 'ALL', label: 'Todos' },
                  { id: 'VERMELHA_RETENCAO', label: 'Retenção' },
                  { id: 'VERMELHA_SEM_RETENCAO', label: 'Vermelha' },
                  { id: 'PRETA', label: 'Preta' },
                  { id: 'LIVRE', label: 'MIP' }
                ].map(t => (
                  <button
                    key={t.id}
                    onClick={() => setTarjaFilter(t.id)}
                    className={`text-xs px-3 py-1.5 rounded-lg font-bold transition-all ${
                      tarjaFilter === t.id
                        ? 'bg-[#142D43] text-white'
                        : 'bg-white border text-gray-600 hover:bg-gray-50'
                    }`}
                  >
                    {t.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Search Input */}
            <div className="relative">
              <Search className="w-5 h-5 text-gray-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="Buscar por nome comercial, princípio ativo ou fabricante (ex: Clavulin, Losartana, Clonazepam)..."
                className="w-full pl-11 pr-4 py-3 bg-white border border-[#E2E8F0] rounded-xl text-sm focus:outline-none focus:ring-2 focus:ring-[#20A89A] shadow-xs"
              />
            </div>

            {/* Medicines List */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredMedicines.map(med => (
                <div
                  key={med.id}
                  onClick={() => setSelectedMedicine(med)}
                  className="bg-white p-5 rounded-2xl border border-[#E2E8F0] hover:border-[#20A89A] hover:shadow-md transition-all cursor-pointer flex flex-col justify-between"
                >
                  <div>
                    <div className="flex justify-between items-start gap-2">
                      <h3 className="font-bold text-base text-[#142D43]">{med.tradeName}</h3>
                      <span className={`text-[10px] font-extrabold px-2.5 py-1 rounded ${
                        med.tarja === 'VERMELHA_RETENCAO' ? 'bg-red-100 text-red-700' :
                        med.tarja === 'PRETA' ? 'bg-gray-900 text-white' :
                        med.tarja === 'LIVRE' ? 'bg-emerald-100 text-emerald-800' :
                        'bg-amber-100 text-amber-800'
                      }`}>
                        {med.tarja.replace('_', ' ')}
                      </span>
                    </div>

                    <div className="text-xs font-semibold text-[#20A89A] mt-1">{med.activeIngredient}</div>
                    <div className="text-xs text-gray-500 mt-0.5">{med.dosageForm} • {med.manufacturer}</div>

                    <div className="mt-4 text-xs text-gray-600 line-clamp-2">
                      <strong>Indicações:</strong> {med.approvedIndications.join(', ')}
                    </div>
                  </div>

                  <div className="mt-5 pt-3 border-t border-gray-100 flex items-center justify-between">
                    <div>
                      <span className="text-[11px] text-gray-400 block">Registro MS: {med.anvisaRegistry}</span>
                      <span className="text-sm font-extrabold text-[#142D43]">PMC: R$ {med.pmcPrice.toFixed(2)}</span>
                    </div>
                    <button className="text-xs font-bold text-[#20A89A] bg-[#E6F7F5] px-3 py-1.5 rounded-lg hover:bg-[#20A89A] hover:text-white transition-all">
                      Ficha Completa
                    </button>
                  </div>
                </div>
              ))}
            </div>

            {/* Modal de Detalhes do Medicamento */}
            {selectedMedicine && (
              <div className="fixed inset-0 bg-black/50 backdrop-blur-xs flex items-center justify-center p-4 z-50">
                <div className="bg-white rounded-2xl max-w-2xl w-full max-h-[85vh] overflow-y-auto p-6 space-y-4">
                  <div className="flex justify-between items-start border-b pb-4">
                    <div>
                      <span className="text-xs text-[#20A89A] font-bold uppercase">{selectedMedicine.manufacturer}</span>
                      <h2 className="text-xl font-black text-[#142D43]">{selectedMedicine.tradeName}</h2>
                      <p className="text-sm font-medium text-gray-600">{selectedMedicine.activeIngredient} ({selectedMedicine.concentration})</p>
                    </div>
                    <button
                      onClick={() => setSelectedMedicine(null)}
                      className="text-gray-400 hover:text-gray-600 text-lg font-bold p-1"
                    >
                      ✕
                    </button>
                  </div>

                  <div className="space-y-4 text-sm">
                    <div className="grid grid-cols-2 gap-3 bg-[#F8FAFC] p-3 rounded-xl text-xs">
                      <div><strong>Registro ANVISA:</strong> {selectedMedicine.anvisaRegistry}</div>
                      <div><strong>Forma Farmacêutica:</strong> {selectedMedicine.dosageForm}</div>
                      <div><strong>PMC (Consumidor):</strong> R$ {selectedMedicine.pmcPrice.toFixed(2)}</div>
                      <div><strong>Genérico:</strong> {selectedMedicine.isGeneric ? 'Sim' : 'Não (Referência)'}</div>
                    </div>

                    <div>
                      <strong className="text-[#142D43] block mb-1">Posologia Oficial Aprovada:</strong>
                      <p className="text-gray-700 bg-gray-50 p-3 rounded-xl text-xs leading-relaxed">{selectedMedicine.posologyApproved}</p>
                    </div>

                    <div>
                      <strong className="text-[#142D43] block mb-1">Indicações Terapêuticas:</strong>
                      <ul className="list-disc pl-5 text-xs text-gray-700 space-y-1">
                        {selectedMedicine.approvedIndications.map((ind, i) => <li key={i}>{ind}</li>)}
                      </ul>
                    </div>

                    <div>
                      <strong className="text-red-600 block mb-1">Interações Medicamentosas Críticas:</strong>
                      <ul className="list-disc pl-5 text-xs text-gray-700 space-y-1">
                        {selectedMedicine.majorInteractions.map((inter, i) => <li key={i}>{inter}</li>)}
                      </ul>
                    </div>

                    <div>
                      <strong className="text-amber-700 block mb-1">Alertas Regulatórios ANVISA:</strong>
                      <ul className="list-disc pl-5 text-xs text-gray-700 space-y-1">
                        {selectedMedicine.clinicalWarnings.map((w, i) => <li key={i}>{w}</li>)}
                      </ul>
                    </div>
                  </div>

                  <div className="pt-4 border-t flex justify-end">
                    <button
                      onClick={() => setSelectedMedicine(null)}
                      className="bg-[#142D43] text-white px-5 py-2 rounded-xl text-xs font-bold hover:bg-[#20A89A] transition-all"
                    >
                      Fechar Consulta
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>
        ) : currentTab === 'calculators' ? (
          /* ======================================================== */
          /* TAB 5: CALCULADORAS CLÍNICAS                             */
          /* ======================================================== */
          <div className="space-y-6">
            <div>
              <h1 className="text-2xl font-black text-[#142D43]">Central de Calculadoras Clínicas Farmacêuticas</h1>
              <p className="text-sm text-gray-500">Ferramentas de cálculo de infusão, diluições, dose pediátrica e função renal (Cockcroft-Gault).</p>
            </div>

            <div className="flex gap-2 border-b border-gray-200 pb-2 overflow-x-auto">
              {[
                { id: 'drops', label: 'Gotejamento & Infusão' },
                { id: 'creatinine', label: 'Clearance de Creatinina' },
                { id: 'dilution', label: 'Diluição (C1·V1 = C2·V2)' },
                { id: 'pediatric', label: 'Dose Pediátrica por Peso' }
              ].map(c => (
                <button
                  key={c.id}
                  onClick={() => setCalcTab(c.id as any)}
                  className={`text-xs sm:text-sm font-bold px-4 py-2 rounded-xl transition-all whitespace-nowrap ${
                    calcTab === c.id ? 'bg-[#142D43] text-white' : 'bg-white text-gray-600 hover:bg-gray-100'
                  }`}
                >
                  {c.label}
                </button>
              ))}
            </div>

            {/* Active Calculator Box */}
            <div className="bg-white rounded-2xl p-6 border border-[#E2E8F0] shadow-sm max-w-xl">
              {calcTab === 'drops' && (
                <div className="space-y-4">
                  <h3 className="font-bold text-lg text-[#142D43]">Cálculo de Gotejamento de Soro / Infusão</h3>
                  <p className="text-xs text-gray-500">Gotas/min = Volume (mL) / (Tempo em horas × 3)</p>
                  
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="text-xs font-bold text-gray-700 block mb-1">Volume da Infusão (mL):</label>
                      <input
                        type="number"
                        value={volDrops}
                        onChange={(e) => setVolDrops(e.target.value)}
                        className="w-full border rounded-xl p-2.5 text-sm"
                      />
                    </div>
                    <div>
                      <label className="text-xs font-bold text-gray-700 block mb-1">Tempo Total (Horas):</label>
                      <input
                        type="number"
                        value={timeDrops}
                        onChange={(e) => setTimeDrops(e.target.value)}
                        className="w-full border rounded-xl p-2.5 text-sm"
                      />
                    </div>
                  </div>

                  {Number(volDrops) > 0 && Number(timeDrops) > 0 && (
                    <div className="mt-4 p-4 bg-[#E6F7F5] border border-[#20A89A]/30 rounded-xl">
                      <div className="text-xs text-gray-600 font-semibold">Resultado Calculado:</div>
                      <div className="text-3xl font-black text-[#178377] mt-1">
                        {Math.round(Number(volDrops) / (Number(timeDrops) * 3))} <span className="text-base font-bold">macrogotas / minuto</span>
                      </div>
                      <div className="text-xs text-gray-500 mt-1">
                        Equipo de microgotas: {Math.round(Number(volDrops) / Number(timeDrops))} microgotas/min
                      </div>
                    </div>
                  )}
                </div>
              )}

              {calcTab === 'creatinine' && (
                <div className="space-y-4">
                  <h3 className="font-bold text-lg text-[#142D43]">Clearance de Creatinina (Cockcroft-Gault)</h3>
                  <p className="text-xs text-gray-500">Estimativa da Taxa de Filtração Glomerular para ajuste posológico.</p>

                  <div className="grid grid-cols-2 gap-4 text-xs font-semibold">
                    <div>
                      <label className="block mb-1">Idade (anos):</label>
                      <input type="number" value={creatAge} onChange={e => setCreatAge(e.target.value)} className="w-full border rounded-xl p-2.5 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Peso Real (kg):</label>
                      <input type="number" value={creatWeight} onChange={e => setCreatWeight(e.target.value)} className="w-full border rounded-xl p-2.5 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Creatinina Sérica (mg/dL):</label>
                      <input type="number" step="0.1" value={creatSerum} onChange={e => setCreatSerum(e.target.value)} className="w-full border rounded-xl p-2.5 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Sexo Biológico:</label>
                      <select value={creatIsFemale ? 'f' : 'm'} onChange={e => setCreatIsFemale(e.target.value === 'f')} className="w-full border rounded-xl p-2.5 text-sm">
                        <option value="m">Masculino</option>
                        <option value="f">Feminino (fator 0.85)</option>
                      </select>
                    </div>
                  </div>

                  {Number(creatSerum) > 0 && (
                    <div className="mt-4 p-4 bg-[#E6F7F5] rounded-xl border border-[#20A89A]/30">
                      {(() => {
                        let clcr = ((140 - Number(creatAge)) * Number(creatWeight)) / (72 * Number(creatSerum));
                        if (creatIsFemale) clcr *= 0.85;
                        return (
                          <div>
                            <div className="text-xs text-gray-600 font-semibold">Filtração Estimada (ClCr):</div>
                            <div className="text-3xl font-black text-[#178377] mt-1">{clcr.toFixed(1)} <span className="text-base font-bold">mL/min</span></div>
                            <div className="text-xs text-gray-600 mt-2 font-medium">
                              {clcr < 30 ? "⚠️ Insuficiência renal moderada/grave. Necessário ajuste de dose de antibióticos e hipoglicemiantes." : "Função renal preservada para a faixa etária."}
                            </div>
                          </div>
                        );
                      })()}
                    </div>
                  )}
                </div>
              )}

              {calcTab === 'dilution' && (
                <div className="space-y-4">
                  <h3 className="font-bold text-lg text-[#142D43]">Diluição de Soluções (C1·V1 = C2·V2)</h3>
                  <div className="grid grid-cols-3 gap-3">
                    <div>
                      <label className="text-xs font-bold block mb-1">C1 (Concentração 1):</label>
                      <input type="number" value={c1} onChange={e => setC1(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                    <div>
                      <label className="text-xs font-bold block mb-1">V1 (Volume 1):</label>
                      <input type="number" value={v1} onChange={e => setV1(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                    <div>
                      <label className="text-xs font-bold block mb-1">C2 (Desejada):</label>
                      <input type="number" value={c2} onChange={e => setC2(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                  </div>
                  {Number(c2) > 0 && (
                    <div className="p-4 bg-[#E6F7F5] rounded-xl border border-[#20A89A]/30">
                      <div className="text-xs text-gray-600">Volume Final Necessário (V2):</div>
                      <div className="text-2xl font-black text-[#178377]">
                        {((Number(c1) * Number(v1)) / Number(c2)).toFixed(2)} mL
                      </div>
                      <div className="text-xs text-gray-500 mt-1">
                        Volume de diluente a adicionar: {(((Number(c1) * Number(v1)) / Number(c2)) - Number(v1)).toFixed(2)} mL
                      </div>
                    </div>
                  )}
                </div>
              )}

              {calcTab === 'pediatric' && (
                <div className="space-y-4">
                  <h3 className="font-bold text-lg text-[#142D43]">Dosagem Pediátrica por Peso Corporal</h3>
                  <div className="grid grid-cols-2 gap-3 text-xs font-bold">
                    <div>
                      <label className="block mb-1">Peso da Criança (kg):</label>
                      <input type="number" value={pedWeight} onChange={e => setPedWeight(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Dose Recomendada (mg/kg/dia):</label>
                      <input type="number" value={pedDose} onChange={e => setPedDose(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Concentração do Xarope (mg/mL):</label>
                      <input type="number" value={pedConc} onChange={e => setPedConc(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                    <div>
                      <label className="block mb-1">Tomadas por Dia:</label>
                      <input type="number" value={pedTakes} onChange={e => setPedTakes(e.target.value)} className="w-full border rounded-xl p-2 text-sm" />
                    </div>
                  </div>

                  {Number(pedWeight) > 0 && Number(pedConc) > 0 && (
                    <div className="p-4 bg-[#E6F7F5] rounded-xl border border-[#20A89A]/30">
                      {(() => {
                        const dailyMg = Number(pedWeight) * Number(pedDose);
                        const takeMg = dailyMg / (Number(pedTakes) || 1);
                        const takeMl = takeMg / Number(pedConc);
                        return (
                          <div>
                            <div className="text-xs text-gray-600">Volume por Tomada:</div>
                            <div className="text-3xl font-black text-[#178377]">{takeMl.toFixed(1)} mL</div>
                            <div className="text-xs text-gray-600 mt-1">
                              {dailyMg.toFixed(0)} mg/dia divididos em {pedTakes}x ({takeMg.toFixed(0)} mg por tomada)
                            </div>
                          </div>
                        );
                      })()}
                    </div>
                  )}
                </div>
              )}
            </div>
          </div>
        ) : currentTab === 'patients' ? (
          /* ======================================================== */
          /* TAB 6: ATENÇÃO FARMACÊUTICA (PACIENTES)                   */
          /* ======================================================== */
          <div className="space-y-6">
            <div className="flex justify-between items-center">
              <div>
                <h1 className="text-2xl font-black text-[#142D43]">Caderno de Atenção Farmacêutica</h1>
                <p className="text-sm text-gray-500">Acompanhamento de hipertensos, diabéticos e conciliação de medicamentos de uso contínuo.</p>
              </div>
              <button
                onClick={() => setShowAddPatient(!showAddPatient)}
                className="bg-[#142D43] hover:bg-[#20A89A] text-white text-xs sm:text-sm font-bold px-4 py-2.5 rounded-xl transition-all flex items-center gap-1.5"
              >
                <Plus className="w-4 h-4" /> Novo Paciente
              </button>
            </div>

            {/* Novo Paciente Form Modal/Collapsible */}
            {showAddPatient && (
              <form onSubmit={handleAddPatient} className="bg-white p-5 rounded-2xl border border-[#20A89A] shadow-md space-y-4">
                <h3 className="font-bold text-[#142D43]">Cadastrar Paciente para Acompanhamento</h3>
                <div className="grid grid-cols-1 sm:grid-cols-3 gap-3">
                  <input
                    type="text"
                    required
                    placeholder="Nome completo do paciente"
                    value={newPatientName}
                    onChange={e => setNewPatientName(e.target.value)}
                    className="border p-2.5 rounded-xl text-sm"
                  />
                  <input
                    type="number"
                    placeholder="Idade (anos)"
                    value={newPatientAge}
                    onChange={e => setNewPatientAge(e.target.value)}
                    className="border p-2.5 rounded-xl text-sm"
                  />
                  <input
                    type="text"
                    placeholder="Pressão Arterial (ex: 130/80)"
                    value={newPatientBP}
                    onChange={e => setNewPatientBP(e.target.value)}
                    className="border p-2.5 rounded-xl text-sm"
                  />
                </div>
                <div className="flex justify-end gap-2">
                  <button
                    type="button"
                    onClick={() => setShowAddPatient(false)}
                    className="px-4 py-2 border rounded-xl text-xs font-bold text-gray-600"
                  >
                    Cancelar
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 bg-[#20A89A] text-white rounded-xl text-xs font-bold hover:bg-[#178377]"
                  >
                    Salvar Prontuário
                  </button>
                </div>
              </form>
            )}

            {/* Patients Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
              {patientList.map(pat => (
                <div key={pat.id} className="bg-white p-5 rounded-2xl border border-[#E2E8F0] shadow-sm space-y-3">
                  <div className="flex justify-between items-start">
                    <div>
                      <h3 className="font-bold text-base text-[#142D43]">{pat.name}</h3>
                      <span className="text-xs text-gray-500">{pat.age} anos • Última consulta: {pat.lastVisitDate}</span>
                    </div>
                    <span className="text-[10px] font-bold px-2 py-0.5 rounded bg-emerald-50 text-emerald-700 border border-emerald-200">
                      {pat.statusBadge}
                    </span>
                  </div>

                  <div className="grid grid-cols-2 gap-2 text-xs bg-[#F8FAFC] p-3 rounded-xl font-medium">
                    <div>
                      <span className="text-gray-400 block text-[10px]">Pressão Arterial:</span>
                      <strong className="text-gray-800 text-sm">{pat.systolicBP}/{pat.diastolicBP} mmHg</strong>
                    </div>
                    <div>
                      <span className="text-gray-400 block text-[10px]">Glicemia Capilar:</span>
                      <strong className="text-gray-800 text-sm">{pat.bloodGlucose} mg/dL</strong>
                    </div>
                  </div>

                  <div className="text-xs text-gray-600">
                    <strong className="text-gray-800 block mb-0.5">Uso Contínuo:</strong>
                    <div className="text-[11px] text-gray-500">{pat.continuousMedicines.join(', ')}</div>
                  </div>

                  <div className="text-xs text-gray-600 italic bg-gray-50 p-2.5 rounded-lg border border-gray-100">
                    "{pat.notes}"
                  </div>
                </div>
              ))}
            </div>
          </div>
        ) : currentTab === 'pathologies' ? (
          /* ======================================================== */
          /* TAB 7: PATOLOGIAS & MAPAS CLÍNICOS                       */
          /* ======================================================== */
          <div className="space-y-6">
            <div>
              <h1 className="text-2xl font-black text-[#142D43]">Mapas Clínicos & Diretrizes de Encaminhamento</h1>
              <p className="text-sm text-gray-500">Protocolos para triagem rápida no balcão e sinais vermelhos (Red Flags) para envio imediato ao SAMU / Emergência.</p>
            </div>

            <div className="space-y-6">
              {pathologies.map(path => (
                <div key={path.id} className="bg-white rounded-2xl border border-[#E2E8F0] p-6 shadow-sm space-y-4">
                  <div className="flex flex-wrap justify-between items-center gap-2 border-b pb-3">
                    <div>
                      <span className="text-xs font-bold text-[#20A89A]">{path.icdCode}</span>
                      <h2 className="text-xl font-bold text-[#142D43]">{path.name}</h2>
                    </div>
                    <span className="text-xs text-gray-500 font-medium">{path.guidelineReference}</span>
                  </div>

                  <p className="text-sm text-gray-700 leading-relaxed">{path.shortSummary}</p>

                  {/* Red Flags Banner */}
                  <div className="bg-red-50 border border-red-200 rounded-xl p-4">
                    <div className="flex items-center gap-2 text-red-700 font-bold text-xs uppercase mb-2">
                      <ShieldAlert className="w-4 h-4" />
                      <span>Sinais Vermelhos (Encaminhamento Imediato)</span>
                    </div>
                    <ul className="text-xs text-red-900 space-y-1">
                      {path.redFlags.map((rf, i) => (
                        <li key={i} className="flex items-center gap-1.5 font-medium">
                          <span>🚨</span> <span>{rf}</span>
                        </li>
                      ))}
                    </ul>
                    <div className="mt-2 text-[11px] text-red-800 font-semibold pt-2 border-t border-red-200">
                      Conduta: {path.referralGuidance}
                    </div>
                  </div>

                  {/* Mental Map Steps */}
                  <div>
                    <h3 className="text-xs font-bold uppercase tracking-wider text-gray-400 mb-3">Fluxograma em 5 Etapas de Atenção Farmacêutica:</h3>
                    <div className="grid grid-cols-1 sm:grid-cols-5 gap-3">
                      {path.mentalMap.map(step => (
                        <div
                          key={step.stepNumber}
                          className={`p-3 rounded-xl border text-xs flex flex-col justify-between ${
                            step.isRedFlag ? 'bg-red-50 border-red-200' : 'bg-[#F8FAFC] border-gray-200'
                          }`}
                        >
                          <div>
                            <span className="font-black text-[#20A89A] text-sm block">#{step.stepNumber}</span>
                            <span className="font-bold text-gray-800 block mt-1">{step.title}</span>
                            <p className="text-[11px] text-gray-600 mt-1 leading-snug">{step.description}</p>
                          </div>
                          <span className="mt-3 text-[9px] font-bold px-2 py-0.5 rounded w-fit bg-white border border-gray-200 text-gray-700">
                            {step.badgeLabel}
                          </span>
                        </div>
                      ))}
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        ) : (
          /* ======================================================== */
          /* TAB 8: PLANOS E ASSINATURAS                              */
          /* ======================================================== */
          <div className="space-y-6 max-w-4xl mx-auto py-6">
            <div className="text-center space-y-2">
              <span className="text-xs font-extrabold text-[#20A89A] uppercase tracking-wider">Planos Comerciais SaaS</span>
              <h1 className="text-3xl font-black text-[#142D43]">Inteligência Farmacêutica para sua Drogaria</h1>
              <p className="text-sm text-gray-500">Escolha o plano ideal para seu consultório, drogaria de bairro ou rede de farmácias.</p>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 pt-6">
              {[
                {
                  name: "INDIVIDUAL",
                  price: "19,90",
                  target: "Estudantes e balconistas",
                  features: ["Consultas ilimitadas de bulas ANVISA", "Calculadoras clínicas completas", "Simulados e Treinamento ANVISA", "1 Usuário ativo"]
                },
                {
                  name: "PROFISSIONAL",
                  price: "39,90",
                  highlight: true,
                  target: "Farmacêuticos RT e Consultores",
                  features: ["Tudo do Individual", "Assistente IA Clínico com RAG ANVISA", "Caderno de Atenção com até 200 pacientes", "Emissão de Declaração de Serviço", "Suporte prioritário"]
                },
                {
                  name: "ESTABELECIMENTO",
                  price: "99,90",
                  target: "Drogarias e Redes",
                  features: ["Tudo do Profissional", "Multi-tenant: Até 10 funcionários de loja", "Auditoria completa de dispensação SNGPC", "Pacientes ilimitados", "Treinamento de equipe de balcão"]
                }
              ].map((pl, i) => (
                <div
                  key={i}
                  className={`bg-white rounded-2xl p-6 border flex flex-col justify-between transition-all ${
                    pl.highlight
                      ? 'border-[#20A89A] shadow-xl ring-2 ring-[#20A89A]/20 scale-105'
                      : 'border-[#E2E8F0] shadow-sm'
                  }`}
                >
                  <div>
                    {pl.highlight && (
                      <span className="text-[10px] font-black px-2.5 py-1 rounded-full bg-[#20A89A] text-white uppercase tracking-wider block w-fit mb-3">
                        Mais Escolhido
                      </span>
                    )}
                    <h3 className="font-black text-xl text-[#142D43]">{pl.name}</h3>
                    <p className="text-xs text-gray-500 mt-1">{pl.target}</p>

                    <div className="mt-4 flex items-baseline gap-1">
                      <span className="text-xs text-gray-400">R$</span>
                      <span className="text-3xl font-black text-[#142D43]">{pl.price}</span>
                      <span className="text-xs text-gray-500">/mês</span>
                    </div>

                    <ul className="mt-6 space-y-2 text-xs text-gray-600">
                      {pl.features.map((f, j) => (
                        <li key={j} className="flex items-center gap-2">
                          <CheckCircle2 className="w-4 h-4 text-[#20A89A] shrink-0" />
                          <span>{f}</span>
                        </li>
                      ))}
                    </ul>
                  </div>

                  <button
                    onClick={() => alert(`Plano ${pl.name} selecionado! Integração de pagamento via PIX/Cartão pronta.`)}
                    className={`mt-6 w-full py-2.5 rounded-xl text-xs font-bold transition-all ${
                      pl.highlight
                        ? 'bg-[#20A89A] hover:bg-[#178377] text-white shadow-md'
                        : 'bg-[#142D43] hover:bg-gray-800 text-white'
                    }`}
                  >
                    Assinar {pl.name}
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="bg-white border-t border-[#E2E8F0] py-6 text-center text-xs text-gray-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row justify-between items-center gap-2">
          <span>© 2026 NEXFAR Plataforma Farmacêutica SaaS. Todos os direitos reservados.</span>
          <span className="text-[11px] text-gray-400">Conformidade ANVISA RDC 20/2011 • Portaria SVS/MS 344/98 • LGPD</span>
        </div>
      </footer>
    </div>
  );
}
