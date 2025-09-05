// reusable selector function
const $ = (sel) => document.querySelector(sel);
const $all = (sel) => document.querySelectorAll(sel);


const toggle = (el, show) => el.classList.toggle("hidden", !show);

const flex = (el) => {
  el.classList.remove("hidden");
  el.classList.add("flex");
};
const unflex = (el) => {
  el.classList.add("hidden");
  el.classList.remove("flex");
};

// Mobile menu
const mobileBtn = $("#mobile-menu-button");
const mobileMenu = $("#mobile-menu");
mobileBtn?.addEventListener("click", () =>
  mobileMenu.classList.toggle("hidden")
);

// Create post modal
const modalCreate = $("#modal-create");

const openCreateButtons = ["#btn-open-create", "#btn-open-create-2"]
  .map((sel) => $(sel))
  .filter(Boolean);

const closeCreateButtons = ["#btn-close-create", "#btn-cancel-create"]
  .map((sel) => $(sel))
  .filter(Boolean);

openCreateButtons.forEach((btn) =>
  btn.addEventListener("click", () => flex(modalCreate))
);

closeCreateButtons.forEach((btn) =>
  btn.addEventListener("click", () => unflex(modalCreate))
);

modalCreate?.addEventListener("click", (e) => {
  if (e.target === modalCreate) unflex(modalCreate);
});

// Sign in modal
const modalSignin = $("#modal-signin");
const btnOpenSignin = $("#btn-open-signin");
const btnCloseSignin = $("#btn-close-signin");


btnOpenSignin?.addEventListener("click", (e) => {
  e.preventDefault();
  flex(modalSignin);
});

btnCloseSignin?.addEventListener("click", () => unflex(modalSignin));

modalSignin?.addEventListener("click", (e) => {
   const signUpBtn =  $("#signing-signup")
  if (e.target === modalSignin) unflex(modalSignin);
  if(e.target===signUpBtn){
  unflex(modalSignin);
    flex(modalSignup);
  }


});

// signup modal
const modalSignup = $("#modal-signup");
const btnOpenSignup=  $("#btn-open-signup")
const btnCloseSignup = $("#close-sign-up")

 btnOpenSignup?.addEventListener("click", (e) => {
  e.preventDefault();
  flex(modalSignup);
});

btnCloseSignup?.addEventListener("click", () => unflex(modalSignup));
modalSignup.addEventListener("click",(e)=>{
 if(e.target==modalSignup) unflex(modalSignup);

 })

 // forgot password modal
 const modalForgotPassword = $("#forgotPasswordModal");
 const btnOpenForgotPassword =  $("#btn-open-forgot-password");
 const btnCloseForgotPassword  = $("#close-forgot-password");

btnOpenForgotPassword?.addEventListener("click", (e) => {
   e.preventDefault();
   unflex(modalSignin);
   flex(modalForgotPassword);
 });
btnCloseForgotPassword?.addEventListener("click", () => unflex(modalForgotPassword));

modalForgotPassword.addEventListener("click",(e)=>{
 if(e.target===modalForgotPassword) unflex(modalForgotPassword);

 })


//  signin

//const signInform = $("#modal-signin form");
//
//signInform.addEventListener("submit", async (e) => {
//  e.preventDefault();
//  const formData = new FormData(signInform);
//  const res = await fetch("/signin", {
//    method: "POST",
//    body: new URLSearchParams(formData)
//  });
//
//  console.log(res);
//});

// signup

//const signUpForm =  $("#modal-signup form");
//signUpForm.addEventListener("submit", async (e) => {
//    e.preventDefault();
//    const formData = new FormData(signUpForm);
//    const res = await fetch("/register", {
//        method: "POST",
//        headers: { "Content-Type": "application/x-www-form-urlencoded" },
//        body: new URLSearchParams(formData)
//    });
//
//    const data = await res.text();
//    console.log(data);
//
//});

// forgot password

//const forgotPasswordForm = document.querySelector("#forgotPasswordModal form");
//
//forgotPasswordForm.addEventListener("submit", async (e) => {
//    e.preventDefault();
//
//    const formData = new FormData(forgotPasswordForm);
//    const res = await fetch("/forgot-password", {
//        method: "POST",
//        headers: { "Content-Type": "application/x-www-form-urlencoded" },
//        body: new URLSearchParams(formData)
//    });
//
//    console.log(res);
//});

// create-post

//modalCreate.addEventListener("submit",(e)=>{
//e.preventDefault();
//const formData = new FormData(modalCreate);
//    const res = await fetch("/create-post", {
//        method: "POST",
//        headers: { "Content-Type": "application/x-www-form-urlencoded" },
//        body: new URLSearchParams(formData)
//    });
//
//    console.log(res);
//})


// file upload trigger

const uploadBtn = $("#uploadFileBtn");
const fileInput =$("#fileInput");

uploadBtn.addEventListener("click", () => {
    fileInput.click(); // open file picker
});

// add link

// const addLinkBtn = $("#addLinkBtn");
// const linkInput = $("#linkInput");
//  // Show/hide link input when "Add link" is clicked
//    addLinkBtn.addEventListener("click", () => {
//        linkInput.classList.toggle("hidden");
//        linkInput.focus();
//    });


 // create account hero button
let createAccountHeroBtn =  $("#create-account-hero");

createAccountHeroBtn?.addEventListener("click",(e)=>{
    e.preventDefault();
    flex(modalSignup);
})

// edit profile modal

let editProfileModal = $("#editProfileModal")
let editProfileModalForm = $("#editProfileModal form")
let editProfileButton = $("#edit-profile")

editProfileButton?.addEventListener("click",(e)=>{
   e.preventDefault();
   flex(editProfileModal);

})

// share profile

let shareProfileModal =  $("#shareProfileModal")
let shareProfileBtn =  $("#share-my-profile")
let closeShareMyProfileBtn = $("#close-my-profile-btn")

shareProfileBtn?.addEventListener("click",(e)=>{
   e.preventDefault();
   flex(shareProfileModal);
})

closeShareMyProfileBtn?.addEventListener("click", () => unflex(shareProfileModal));


// create comment modal
const createCommentBtn =  $("#create-comment-btn")
const commentModal =  $("#commentModal")

createCommentBtn?.addEventListener("click",(e)=>{
                    e.preventDefault();
                    flex(commentModal);
  })

// list of comments
$all(".show-comments-btn").forEach((btn) => {
    btn?.addEventListener("click", (e) => {
        const article = btn.closest("article");
        const commentsList = article.querySelector(".comments-list");
        const isHidden = commentsList.classList.contains("hidden");
        toggle(commentsList, isHidden);
        btn.textContent = isHidden ? "Hide" : "Show";
    });
});

// preview discussion feature

const previewBtn = $("#previewBtn");
const discussionText = $("#discussionText");
const discussionPreview = $("#discussionPreview");

previewBtn?.addEventListener("click", () => {
        const content = discussionText.value.trim();
        if(content.length === 0){
            discussionPreview.classList.add("hidden");
            discussionPreview.textContent = "";
            return;
        }
        discussionPreview.textContent = content;
        discussionPreview.classList.remove("hidden");
        discussionPreview.scrollIntoView({ behavior: "smooth" });
});


// like and unlike
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".like-btn").forEach(btn => {
        btn.addEventListener("click", () => {
            const feedId = btn.dataset.id;
            fetch(`/feeds/like/${feedId}`, { method: "POST" })
                .then(res => res.json())
                .then(data => {
                    btn.querySelector(".like-count").innerText = data.likes;
                })
                .catch(err => console.error("Error:", err));
        });
    });
});