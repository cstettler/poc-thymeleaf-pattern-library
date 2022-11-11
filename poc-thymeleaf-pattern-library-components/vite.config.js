import { defineConfig } from 'vite'
import copy from 'rollup-plugin-copy'

const componentResourceRootPath = 'src/main/resources/templates/pl';
const componentBaseName = 'poc-thymeleaf-pattern-library-components';
const targetPath = 'target/classes/static/pl';

export default defineConfig({
  build: {
    outDir: targetPath,
    emptyOutDir: false,
    lib: {
      entry: [
        path(componentResourceRootPath, '/index.js'),
        path(componentResourceRootPath, '/index.css')
      ],
      formats: ['es'],
      fileName: componentBaseName
    },
    rollupOptions: {
      output: {
        assetFileNames: `${componentBaseName}.[ext]`
      },
      plugins: [
        copy({
          targets: [
            {
              src: path(componentResourceRootPath, '/**/*.png'),
              dest: targetPath,
              rename: (name, extension, fullPath) => `${pathAfter(fullPath, componentResourceRootPath)}`
            }
          ],
        })
      ]
    }
  }
})

function path(rootPath, filePath) {
  return rootPath + filePath;
}

function pathAfter(fullPath, relativePathRoot) {
  return fullPath.substring(fullPath.indexOf(relativePathRoot) + relativePathRoot.length)
}
