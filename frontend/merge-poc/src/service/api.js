import clientdata from "../data/poc_api_adm_teams.json";
export const fetchClientData = async () => {
  return clientdata;
  /*
  const response = await fetch('https://api.example.com/data');
  if (!response.ok) {
    throw new Error('Network response was not ok');
  }
  const data = await response.json();
  return data;
  */
};

export const fetchToken = async (data) => {
  try {
    const response = await fetch("/service/api/merge/link-token", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error("Network response was not ok: " + response.statusText);
    }

    const responseData = await response.json();
    console.log("Success:", responseData);
    return responseData; // Return the data for further use
  } catch (error) {
    console.error("Error:", error);
    throw error; // Optionally rethrow the error for further handling
  }
};

export const exchangeToken = async (data) => {
  try {
    const response = await fetch("/service/api/merge/exchange-access-token", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error("Network response was not ok: " + response.statusText);
    }

    const responseData = await response.json();
    console.log("Success:", responseData);
    return responseData; // Return the data for further use
  } catch (error) {
    console.error("Error:", error);
    throw error; // Optionally rethrow the error for further handling
  }
};

export const fetchIntegrationEmployee = async (clientId) => {
  const response = await fetch(
    `/service/api/merge/${clientId}/integration_employees`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  if (!response.ok) {
    throw new Error("Network response was not ok: " + response.statusText);
  }

  const responseData = await response.json();
  console.log("Success:", responseData);
  return responseData; // Return the data for further use
};

export const fetchPocEmployee = async (clientId) => {
  const response = await fetch(
    `/service/api/merge/${clientId}/poc_employees`,
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  if (!response.ok) {
    throw new Error("Network response was not ok: " + response.statusText);
  }

  const responseData = await response.json();
  console.log("Success:", responseData);
  return responseData; // Return the data for further use
};

export const bindingEmployee = async (data) => {
  try {
    const response = await fetch("/service/api/merge/binding", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      throw new Error("Network response was not ok: " + response.statusText);
    }

    const responseData = await response.json();
    console.log("Success:", responseData);
    return responseData; // Return the data for further use
  } catch (error) {
    console.error("Error:", error);
    throw error; // Optionally rethrow the error for further handling
  }
};
