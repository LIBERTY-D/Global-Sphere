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

// Mobile menu toggle
const mobileBtn = $("#mobile-menu-button");
const mobileMenu = $("#mobile-menu");
mobileBtn?.addEventListener("click", () => toggle(mobileMenu, mobileMenu.classList.contains("hidden")));

// Utility function to handle multiple triggers for a modal
function setupModal(openSelectorList, modalEl, closeSelectorList = []) {
  openSelectorList.forEach(sel => {
    $all(sel).forEach(btn => btn.addEventListener("click", (e) => {
      e.preventDefault();
      flex(modalEl);
    }));
  });

  closeSelectorList.forEach(sel => {
    $all(sel).forEach(btn => btn.addEventListener("click", () => unflex(modalEl)));
  });

  modalEl?.addEventListener("click", e => {
    if (e.target === modalEl) unflex(modalEl);
  });
}

// Setup modals using data attributes
setupModal(['[data-open="create-post"]'], $("#modal-create"), ['#btn-close-create','#btn-cancel-create']);
setupModal(['[data-open="signin"]'], $("#modal-signin"), ['#btn-close-signin']);
setupModal(['[data-open="signup"]'], $("#modal-signup"), ['#close-sign-up']);
setupModal(['[data-open="forgot-password"]'], $("#forgotPasswordModal"), ['#close-forgot-password']);
setupModal(['[data-open="edit-profile"]'], $("#editProfileModal"), ['#close-edit-profile']);
setupModal(['[data-open="share-profile"]'], $("#shareProfileModal"), ['#close-my-profile-btn']);
setupModal(['[data-open="create-comment"]'], $("#commentModal"));

// Additional triggers
$("#create-account-hero")?.addEventListener("click", e => { e.preventDefault(); flex($("#modal-signup")); });

// Preview discussion
$("#previewBtn")?.addEventListener("click", () => {
  const content = $("#discussionText").value.trim();
  const preview = $("#discussionPreview");
  if (!content) {
    preview.classList.add("hidden");
    preview.textContent = "";
    return;
  }
  preview.textContent = content;
  preview.classList.remove("hidden");
  preview.scrollIntoView({ behavior: "smooth" });
});

// Like/unlike functionality
document.addEventListener("DOMContentLoaded", () => {
  $all(".like-btn").forEach(btn => {
    btn.addEventListener("click", () => {
      const feedId = btn.dataset.id;
      fetch(`/feeds/like/${feedId}`, { method: "POST" })
        .then(res => res.json())
        .then(data => btn.querySelector(".like-count").innerText = data.likes)
        .catch(err => console.error("Error:", err));
    });
  });
});

// File upload trigger
$("#uploadFileBtn")?.addEventListener("click", () => $("#fileInput").click());


// comments

const commentsBtn =  $('.show-comments-btn')
const commentList =  $(".comments-list")

commentsBtn?.addEventListener("click",()=>{
   toggle(commentList, commentList.classList.contains("hidden"))
})


// share
const createPostBtn =  $("#btn-open-create-2")

createPostBtn?.addEventListener("click",()=>{
   flex($("#modal-create"))
})

//search input
  const searchInput = $("#searchInput");
  const searchBtn = $("#searchBtn");
  searchInput?.addEventListener("input", () => {
        searchBtn.disabled = ! searchInput.value.trim();
   });
// edit modal
const followingIdProfileBtn = $("#following-id-profile")
const followersIdProfileBtn =  $("#followers-id-profile")
const followModal = $('#followModal');
const closeBtnFollowersFollow = $("#followers-following-close-id-btn")
const closeXFollowersFollow = $("#followers-following-close-id-x")

followingIdProfileBtn?.addEventListener("click",()=>{
 toggle(followModal, followModal.classList.contains("hidden"))
})


followersIdProfileBtn?.addEventListener("click",()=>{
 toggle(followModal, followModal.classList.contains("hidden"))
})


closeBtnFollowersFollow?.addEventListener("click",()=>{
 unflex(followModal )
})

closeXFollowersFollow ?.addEventListener("click",()=>{
 unflex(followModal )
})

const followersTabBtn =  $("#followersTab")
const followingTabBtn =  $("#followingTab")

followersTabBtn?.addEventListener("click",()=>{
    showTab('followers')
})

followingTabBtn?.addEventListener("click",()=>{
    showTab('following')
})

 function showTab(tab) {
        const followersContent = $('#followersContent');
        const followingContent = $('#followingContent');
        const followersTab = $('#followersTab');
        const followingTab = $('#followingTab');

        if(tab === 'followers'){
            followersContent.classList.remove('hidden');
            followingContent.classList.add('hidden');
            followersTab.classList.add('text-primary-600', 'border-b-2', 'border-primary-600');
            followersTab.classList.remove('text-gray-500');
            followingTab.classList.remove('text-primary-600', 'border-b-2', 'border-primary-600');
            followingTab.classList.add('text-gray-500');
        } else {
            followersContent.classList.add('hidden');
            followingContent.classList.remove('hidden');
            followingTab.classList.add('text-primary-600', 'border-b-2', 'border-primary-600');
            followingTab.classList.remove('text-gray-500');
            followersTab.classList.remove('text-primary-600', 'border-b-2', 'border-primary-600');
            followersTab.classList.add('text-gray-500');
        }
    }

