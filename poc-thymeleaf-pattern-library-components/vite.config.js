import { defineConfig } from 'vite'
import postcssNesting from 'postcss-nesting';

export default defineConfig({
  build: {
    outDir: 'target/classes/static',
    lib: {
      entry: ['src/main/resources/templates/components/index.js', 'src/main/resources/templates/components/index.css'],
      formats: ['es'],
      fileName: 'poc-thymeleaf-pattern-library-components'
    },
    rollupOptions: {
      output: {
        assetFileNames: "poc-thymeleaf-pattern-library-components.[ext]"
      },
    }
  }
})
