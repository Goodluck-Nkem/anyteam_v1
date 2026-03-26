import { fetch_text, api_request, loadCSS } from "./util.js";
import { router } from "./router.js"

export default async function _(markup, placeholder) {
  console.log("Entering Home page");
  document.getElementById("main").innerHTML = "<h3>Loading Home Page...</h3>";
  await loadCSS("/css/home.css");

  /* load needed resources */
  if (!markup.home)
    markup.home = await fetch_text("/js/html/home.html");

  /* set page UI */
  document.getElementById("main").innerHTML = `
      <h1>Home Page</h1>
      ${markup.home}
    `;

}

export function pageCleanup() {
  console.log("Leaving Home page");
}
