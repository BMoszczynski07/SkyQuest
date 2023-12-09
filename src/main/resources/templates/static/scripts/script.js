let passModal;

let conditions;

const userLogin = (e) => {
  // fetch to API
};

const userRegister = (e) => {
  // fetch to API
};

const createPassModal = () => {
  const passModal = document.createElement("ul");
  passModal.classList.add("login__pass-conditions");

  conditions = [
    {
      valid: false,
      condition: "Hasło musi mieć przynajmniej osiem znaków",
    },
    {
      valid: true,
      condition: "Hasło musi zawierać małe i wielkie litery",
    },
    {
      valid: false,
      condition: "Hasło musi posiadać znak specjalny",
    },
  ];

  for (let i = 0; i < conditions.length; i++) {
    const conditionEl = document.createElement("li");
    conditionEl.classList.add("login__pass-condition");

    const conditionIcon = document.createElement("i");

    if (conditions[i].valid) {
      conditionIcon.classList.add("fa-solid");
      conditionIcon.classList.add("fa-check");
      conditionIcon.classList.add("login__pass-fa");
    } else {
      conditionIcon.classList.add("fa-solid");
      conditionIcon.classList.add("fa-xmark");
      conditionIcon.classList.add("login__pass-fa");
    }
    conditionIcon.classList.add(
      conditions[i].valid
        ? "fa-solid.fa-check.login__pass-fa"
        : "fa-solid.fa-xmark.login__pass-fa"
    );

    const conditionLabel = document.createElement("span");
    conditionLabel.classList.add("login__pass-condition__label");
    conditionLabel.textContent = conditions[i].condition;

    conditionEl.appendChild(conditionIcon);
    conditionEl.appendChild(conditionLabel);

    passModal.appendChild(conditionEl);
  }

  return passModal;
};

const verifyPass = (pass) => {};

document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.querySelector(".login__form");

  const passInputs = document.querySelectorAll(
    ".login__form > .login__field > .login__input[type=password]"
  );

  passInputs.forEach((passInput) => {
    passInput.addEventListener("focus", (e) => {
      passModal = createPassModal();

      e.target.addEventListener("input", () => {
        verifyPass(e.target.value);
      });

      const field = document.querySelector(
        `.login__field[data-field=${e.target.id}]`
      );

      field.appendChild(passModal);
    });

    passInput.addEventListener("blur", () => {
      passModal.remove();
    });
  });

  loginForm.addEventListener("submit", (e) => {});
});
