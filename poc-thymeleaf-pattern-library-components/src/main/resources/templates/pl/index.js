import { Application as StimulusApplication } from "stimulus";
import { AlertController } from './alert/alert.js'
import { greet } from './button/button.js'
import './collapsible/collapsible.js'

export {
  StimulusApplication,
  registerGlobals,
  registerStimulusControllers,
}

function registerGlobals(container) {
  container.greet = greet;
}

function registerStimulusControllers(stimulusApplication) {
  stimulusApplication.register('alert', AlertController)
}