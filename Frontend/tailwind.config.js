/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'tanishq-green':       '#0a3d2e',
        'tanishq-green-light': '#4a7c59',
        'tanishq-gold':        '#b8960c',
        'tanishq-sage':        '#8a9e7e',
        'tanishq-light':       '#c8d5b9',
      },
      backgroundImage: {
        'tanishq-gradient': 'linear-gradient(to bottom, #0a3d2e, #4a7c59)',
      },
      fontFamily: {
        serif: ['Georgia', 'serif'],
      }
    },
  },
  plugins: [],
}