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


// edit resource
let modalEditResource =  $("#modal-edit-resource")
let modalEditResourceClose  =$("#modal-edit-resource-close")
let  modalEditResourceOpenBtn =   $("#modal-edit-resource-btn")

modalEditResourceOpenBtn?.addEventListener("click",(e)=>{
   e.preventDefault()
   flex(modalEditResource)
})


modalEditResourceClose?.addEventListener("click",(e)=>{
   e.preventDefault()
   unflex(modalEditResource)
})