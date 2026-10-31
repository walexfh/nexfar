/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        nexfarNavy: '#142D43',
        nexfarNavyDark: '#0D1E2D',
        nexfarNavyLight: '#1C3E5C',
        nexfarTeal: '#20A89A',
        nexfarTealLight: '#E6F7F5',
        nexfarTealDark: '#178377',
        nexfarIce: '#F4F7FA',
        nexfarCardBorder: '#E2E8F0',
        tarjaVermelha: '#DC2626',
        tarjaPreta: '#18181B',
        tarjaAmarela: '#CA8A04',
      },
    },
  },
  plugins: [],
}
