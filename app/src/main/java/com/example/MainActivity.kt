package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.data.NexfarRepository
import com.example.model.Medicine
import com.example.model.Organization
import com.example.ui.components.NexfarBottomBar
import com.example.ui.components.NexfarTab
import com.example.ui.components.NexfarTopBar
import com.example.ui.screens.*
import com.example.ui.theme.NexfarTheme

enum class SubScreen {
    NONE,
    LIBRARY,
    PATHOLOGIES,
    QUIZ,
    PATIENTS,
    PLANS,
    PROFILE
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexfarTheme {
                NexfarApp()
            }
        }
    }
}

@Composable
fun NexfarApp() {
    var currentOrg by remember { mutableStateOf(NexfarRepository.organizations.first()) }
    var isAdminMode by remember { mutableStateOf(false) }
    var currentTab by remember { mutableStateOf(NexfarTab.DASHBOARD) }
    var currentSubScreen by remember { mutableStateOf(SubScreen.NONE) }
    var selectedMedicineForMonograph by remember { mutableStateOf<Medicine?>(null) }

    // Android Hardware / Gesture Back Button Handling
    BackHandler(enabled = isAdminMode || currentSubScreen != SubScreen.NONE || currentTab != NexfarTab.DASHBOARD) {
        when {
            currentSubScreen != SubScreen.NONE -> {
                currentSubScreen = SubScreen.NONE
            }
            isAdminMode -> {
                isAdminMode = false
            }
            currentTab != NexfarTab.DASHBOARD -> {
                currentTab = NexfarTab.DASHBOARD
            }
        }
    }

    // If user is not logged in and not exploring master admin, display LoginScreen for clean credentials
    if (!NexfarRepository.isUserLoggedIn && !isAdminMode) {
        LoginScreen(
            onLoginSuccess = {
                currentTab = NexfarTab.DASHBOARD
                currentSubScreen = SubScreen.NONE
            },
            onNavigateToMasterAdmin = {
                isAdminMode = true
            }
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NexfarTopBar(
                currentOrganization = currentOrg,
                organizations = NexfarRepository.organizations,
                onSelectOrganization = { selectedOrg ->
                    currentOrg = selectedOrg
                    if (selectedOrg.id == "org_admin") {
                        isAdminMode = true
                    }
                },
                isAdminMode = isAdminMode,
                onToggleAdminMode = {
                    isAdminMode = !isAdminMode
                    currentSubScreen = SubScreen.NONE
                },
                onOpenSearch = {
                    currentTab = NexfarTab.MEDICINES
                    currentSubScreen = SubScreen.NONE
                },
                onOpenProfile = {
                    currentSubScreen = SubScreen.PROFILE
                }
            )
        },
        bottomBar = {
            if (!isAdminMode) {
                NexfarBottomBar(
                    selectedTab = currentTab,
                    onTabSelected = { tab ->
                        currentTab = tab
                        currentSubScreen = SubScreen.NONE
                    }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // If in Admin SaaS Mode
                isAdminMode -> {
                    AdminPanelScreen(
                        onExitAdminMode = { isAdminMode = false }
                    )
                }

                // If in a deep Sub-Screen
                currentSubScreen == SubScreen.LIBRARY -> {
                    LibraryScreen()
                }

                currentSubScreen == SubScreen.PATHOLOGIES -> {
                    PathologiesScreen()
                }

                currentSubScreen == SubScreen.QUIZ -> {
                    QuizScreen()
                }

                currentSubScreen == SubScreen.PATIENTS -> {
                    PatientTrackerScreen()
                }

                currentSubScreen == SubScreen.PLANS -> {
                    PlansScreen()
                }

                currentSubScreen == SubScreen.PROFILE -> {
                    ProfileScreen(
                        currentOrganization = currentOrg,
                        onNavigateToPlans = { currentSubScreen = SubScreen.PLANS },
                        onLogout = {
                            NexfarRepository.logoutUser()
                            currentSubScreen = SubScreen.NONE
                        },
                        onNavigateToAdmin = {
                            isAdminMode = true
                            currentSubScreen = SubScreen.NONE
                        }
                    )
                }

                // Primary Bottom Tabs
                currentTab == NexfarTab.DASHBOARD -> {
                    DashboardScreen(
                        currentOrganization = currentOrg,
                        onNavigateToAssistant = { currentTab = NexfarTab.ASSISTANT },
                        onNavigateToMedicines = { currentTab = NexfarTab.MEDICINES },
                        onNavigateToCalculators = { currentTab = NexfarTab.CALCULATORS },
                        onNavigateToLibrary = { currentSubScreen = SubScreen.LIBRARY },
                        onNavigateToPathologies = { currentSubScreen = SubScreen.PATHOLOGIES },
                        onNavigateToQuiz = { currentSubScreen = SubScreen.QUIZ },
                        onNavigateToPatientTracker = { currentSubScreen = SubScreen.PATIENTS },
                        onNavigateToPlans = { currentSubScreen = SubScreen.PLANS },
                        onSelectMedicine = { med ->
                            selectedMedicineForMonograph = med
                            currentTab = NexfarTab.MEDICINES
                        }
                    )
                }

                currentTab == NexfarTab.ASSISTANT -> {
                    AssistantScreen()
                }

                currentTab == NexfarTab.MEDICINES -> {
                    MedicinesScreen(
                        initialSelectedMedicine = selectedMedicineForMonograph
                    )
                }

                currentTab == NexfarTab.CALCULATORS -> {
                    CalculatorsScreen()
                }

                currentTab == NexfarTab.HUB -> {
                    HubScreen(
                        currentOrganization = currentOrg,
                        onNavigateToLibrary = { currentSubScreen = SubScreen.LIBRARY },
                        onNavigateToPathologies = { currentSubScreen = SubScreen.PATHOLOGIES },
                        onNavigateToQuiz = { currentSubScreen = SubScreen.QUIZ },
                        onNavigateToPatients = { currentSubScreen = SubScreen.PATIENTS },
                        onNavigateToPlans = { currentSubScreen = SubScreen.PLANS },
                        onNavigateToProfile = { currentSubScreen = SubScreen.PROFILE },
                        onNavigateToAdmin = { isAdminMode = true }
                    )
                }
            }
        }
    }
}
