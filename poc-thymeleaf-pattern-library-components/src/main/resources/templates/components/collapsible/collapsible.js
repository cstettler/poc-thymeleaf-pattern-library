export class Collapsible extends HTMLElement {
  connectedCallback() {
    const initiallyOpen = this.getAttribute('open') !== null;
    this.titleElement = this.contents.item(1);
    this.contentElement = this.contents.item(2);

    this.titleClickHandler = () => {
      this.contentElement.hidden = !this.contentElement.hidden;
    };

    this.titleElement.addEventListener('click', this.titleClickHandler)

    this.contentElement.hidden = !initiallyOpen;
  }

  disconnectedCallback() {
    this.titleElement.removeEventListener('click', this.titleClickHandler)
  }

  get contents() {
    let elementNodeListOf = this.querySelectorAll('*');
    console.log(elementNodeListOf);
    return elementNodeListOf;
  }
}

customElements.define('pl-collapsible', Collapsible);