(ns featha.app
  (:require
   ["@logseq/libs"]
   [promesa.core :as p]
   [cljs.core.async :refer [go]]
   [cljs.core.async.interop :refer-macros [<p!]]))

(defn slash-command []
  (js/console.log "Hello from slash-command"))

;
; use promesa.core
;
(defn block-context-menu-block-promesa [^js/IHookEvent e]
  (js/console.log "Hello from block-context-menu-block-promesa")
  (p/-> (js/logseq.Editor.getBlock (.-uuid e))
        (js/console.log)))

(defn block-context-menu-block-promesa-let [^js/IHookEvent e] 
  (js/console.log "Hello from block-context-menu-block-promesa-let")
  (p/let [block (js/logseq.Editor.getBlock (.-uuid e))
          page (js/logseq.Editor.getPage (.-id (.-page block)))]
    (js/console.log block)
    (js/console.log page)))

;
; use core.async
;
(defn block-context-menu-block-async [^js/IHookEvent e]
  (js/console.log "block-context-menu-block-async")
  (go 
    (let [block (<p! (js/logseq.Editor.getBlock (.-uuid e)))
          page (<p! (js/logseq.Editor.getPage (.-id (.-page block))))]
      (try
        (js/console.log block)
        (js/console.log page)
        (catch js/Error e (js/console.error e))))))
;
; Interact with Promise without external library
;

; use ^js type-hint to access js properties, 
; in this case, keep the compiler happy to remove the warning 
; "Cannot infer target type in expression (. e -uuid)"
(defn block-context-menu-item-action [^js/IHookEvent e]
  (js/console.log "block-context-menu-item-action --- ")
  (js/console.log e))

; use core.async to make sure async codes are executed.
(defn block-context-menu-get-page [^js/IHookEvent e]
  (js/console.log "block-context-menu-get-all-blocks-on-the-page --- ")
  (js/console.log e)
  (-> (js/logseq.Editor.getBlock (.-uuid e))
      (.then (fn [block]
               (js/console.log block)
               (-> (js/logseq.Editor.getPage (.-id (.-page block)))
                   (.then js/console.log))))))


(defn main []
  ; print to console
  (js/console.log "Hello from featha main")
  ; show message in logseq UI
  (js/logseq.App.showMsg "❤️  Message from featha main")
  ; register slash command
  (js/logseq.Editor.registerSlashCommand "featha" slash-command)
  ; register block context menu item use fn.
  (js/logseq.Editor.registerBlockContextMenuItem "featha (block)" (fn [e] (block-context-menu-item-action e)))
  ; register block context menu to process block
  (js/logseq.Editor.registerBlockContextMenuItem "featha (page)" (fn [e] (block-context-menu-get-page e)))
  ; process block with core.async
  (js/logseq.Editor.registerBlockContextMenuItem "featha (block with async)" (fn [e] (block-context-menu-block-async e)))
  ; proecess block with promesa 
  (js/logseq.Editor.registerBlockContextMenuItem "featha (block with promesa)" (fn [e] (block-context-menu-block-promesa e)))
  ; proecess block with promesa 
  (js/logseq.Editor.registerBlockContextMenuItem "featha (block with promesa let)" (fn [e] (block-context-menu-block-promesa-let e))))


(defn init []
  (-> (js/logseq.ready) ; return a promise
      (.then main)      ; then on the promise
      (.catch #(js/console.error))))