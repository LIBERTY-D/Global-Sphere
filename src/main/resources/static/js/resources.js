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

//create resource
let modalCreateResource=$("#modal-create-resource")
let modalCreateResourceCloseBtn = $("#modal-create-resource-close")
let createResourceBtn =  $("#create-resource-btn")
createResourceBtn?.addEventListener("click",(e)=>{
     e.preventDefault();
     flex(modalCreateResource)

})
modalCreateResourceCloseBtn?.addEventListener("click",(e)=>{
   e.preventDefault()
   unflex( modalCreateResource)
})