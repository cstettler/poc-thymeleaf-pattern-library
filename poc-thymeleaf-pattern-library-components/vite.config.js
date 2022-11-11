import { defineConfig } from 'vite'
import copy from 'rollup-plugin-copy'

export default defineConfig({
  build: {
    outDir: 'target/classes/static',
    emptyOutDir: false,
    lib: {
      entry: ['src/main/resources/templates/components/index.js', 'src/main/resources/templates/components/index.css'],
      formats: ['es'],
      fileName: 'poc-thymeleaf-pattern-library-components'
    },
    rollupOptions: {
      output: {
        assetFileNames: "poc-thymeleaf-pattern-library-components.[ext]"
      },
      plugins: [
        copy({
          targets: [
            {
              src: 'src/main/resources/templates/components/**/*.png',
              dest: 'target/classes/static',
              rename: (name, extension, fullPath) => `${pathAfter(fullPath, 'src/main/resources/templates')}`
            }
          ],
        })
      ]
    }
  }
})

function pathAfter(fullPath, relativePathRoot) {
  console.log(fullPath)
  console.log(relativePathRoot)
  return fullPath.substring(fullPath.indexOf(relativePathRoot) + relativePathRoot.length)
}
