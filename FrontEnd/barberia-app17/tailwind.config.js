/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      fontFamily: {
        serif: ['Lora', 'serif']
      },
      colors: {
        gold: {
          '50': '#f6f5f0',
          '100': '#e8e6d9',
          '200': '#d2cfb6',
          '300': '#b8b18c',
          '400': '#a3976c',
          '500': '#96885f',
          '600': '#7f6e4f',
          '700': '#675741',
          '800': '#584a3b',
          '900': '#4d4136',
          '950': '#2b231d',
        },
      }
    },
  },
  plugins: [],
}
