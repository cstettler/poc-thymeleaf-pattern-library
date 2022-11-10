window.resizeFrameHeightToContent = (frame) => {
  updateFrameHeightToContent(frame);

  const mutationObserver = new MutationObserver(() => {
    updateFrameHeightToContent(frame);
  });

  mutationObserver.observe(frame.contentWindow.document.body, {
    characterData: true,
    attributes: true,
    childList: true,
    subtree: true
  })
}

function updateFrameHeightToContent(frame) {
  const height = frame.contentWindow.document.body.scrollHeight;
  frame.style.height = height + "px";
}
