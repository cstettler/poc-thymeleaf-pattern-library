import { Controller } from "stimulus";

export class AlertController extends Controller {

  static targets = [ "alert"]

  close() {
    this.alertTarget.hidden = true;
  }
}
