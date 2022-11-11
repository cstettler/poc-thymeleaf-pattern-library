export class Collapsible extends HTMLElement {
  connectedCallback() {
    const initiallyOpen = this.getAttribute('open') !== null;

    this.titleClickHandler = () => {
      this.contentElement.hidden = !this.contentElement.hidden;
      this.updateArrow();
    };

    this.titleElement.addEventListener('click', this.titleClickHandler)

    this.contentElement.hidden = !initiallyOpen;
    this.updateArrow();
  }

  disconnectedCallback() {
    this.titleElement.removeEventListener('click', this.titleClickHandler)
  }

  updateArrow() {
    if (this.contentElement.hidden) {
      this.arrowElement.classList.remove('pl-collapsible-arrow-expanded');
      this.arrowElement.classList.add('pl-collapsible-arrow-collapsed');
    } else {
      this.arrowElement.classList.remove('pl-collapsible-arrow-collapsed');
      this.arrowElement.classList.add('pl-collapsible-arrow-expanded');
    }
  }

  get titleElement() {
    return this.querySelector(".pl-collapsible-title");
  }

  get arrowElement() {
    return this.querySelector(".pl-collapsible-arrow");
  }

  get contentElement() {
    return this.querySelector(".pl-collapsible-content");
  }
}

customElements.define('pl-collapsible', Collapsible);