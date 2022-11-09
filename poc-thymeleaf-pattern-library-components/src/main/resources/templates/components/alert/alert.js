import { Controller } from "stimulus";

export class AlertController extends Controller {

  static targets = [ "alert"]

  close() {
    console.log("close");
    this.alertTarget.hidden = true;
  }
}
