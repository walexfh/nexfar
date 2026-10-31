import type { CapacitorConfig } from '@capacitor/cli';

const config: CapacitorConfig = {
  appId: 'com.aistudio.nexfar.saas',
  appName: 'NEXFAR',
  webDir: 'dist',
  bundledWebRuntime: false,
  server: {
    androidScheme: 'https'
  },
  plugins: {
    SplashScreen: {
      launchShowDuration: 2000,
      backgroundColor: "#142D43",
      showSpinner: false,
      androidSpinnerStyle: "large",
      spinnerColor: "#20A89A"
    }
  }
};

export default config;
