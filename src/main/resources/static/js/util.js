/**
 * Fetches plain markup text from "url".
 * 
 * On error return an error markup.
 * 
 * @param {string} url 
 * @returns {Promise<string>}
 */
export async function fetch_text(url) {
  let file_contents = null;
  /* file access way instead of direct template */
  try {
    const res = await fetch(url);
    if (res.ok)
      file_contents = await res.text();
  }
  catch (e) { }

  if (!file_contents)
    file_contents = `<div>${url}: Resource not found</div>`;

  return file_contents;
}
/**
 * Attaches CSS if not already
 * 
 * @param {string} url 
 * @returns {Promise<string>}
 */
export async function loadCSS(href) {
  if (document.querySelector(`link[href="${href}"]`))
    return;

  const link = document.createElement("link");
  link.rel = "stylesheet";
  link.href = href;
  document.head.appendChild(link);
}

/**
 * Centralized helper for making HTTP requests to Flask API
 * 
 * This returns a JSON or null, depending on the HTTP response code.
 * 
 * @param {string} url 
 * @param {string} method 
 * @param {JSON} body 
 * @returns {Promise<JSON>}
 */
export async function api_request(url, method = "GET", body) {
  // Perform HTTP request
  const res = await fetch(url, {
    method,
    headers: {
      // Tell Flask we are sending JSON
      "Content-Type": "application/json"
    },
    // Only send body if provided
    body: body ? JSON.stringify(body) : undefined
  });

  // Parse JSON response as long as status isn't HTTP_204_NO_CONTENT
  if (res.status === 204)
    return null;
  const data = await res.json();

  // If HTTP status is not 2xx (SUCCESS), treat as error (data will be available in catch clause)
  if (!res.ok) {
    throw data;
  }

  // Return parsed data on success
  return data;
}
