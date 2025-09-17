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


//COMMENTS PAGE DASHBOARD
const editCommentBtnId = $("#edit-comment-btn-id")
editCommentBtnId?.addEventListener("click",()=>{
  const modal = $("#edit-comment-modal")
  toggle(modal,modal.classList.contains("hidden"));
})

// Close modal which is called in the html
 function closeEditModal() {
      unflex($("#edit-comment-modal"))
 }


 // FEEDS PAGE DASHBOARD
function openEditFeedModal() {
    flex($('#edit-feed-modal'))
}

function closeEditFeedModal() {
   unflex($('#edit-feed-modal'))
}
//  File upload trigger FEEDS
$("#uploadFileBtn-edit")?.addEventListener("click", () => $("#fileInput-edit").click());


// RESOURCES PAGE DASHBOARD


// edit resource
let modalEditResource =  $("#modal-edit-resource")
let modalEditResourceClose  =$("#modal-edit-resource-close")
let  modalEditResourceOpenBtn =   $("#modal-edit-resource-btn")

modalEditResourceOpenBtn?.addEventListener("click",(e)=>{

   flex(modalEditResource)
})


modalEditResourceClose?.addEventListener("click",(e)=>{

   unflex(modalEditResource)
})