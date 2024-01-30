(ns featha.app
  (:require
   ["@logseq/libs"]))

(defn slash-command []
  (js/console.log "Hello from slash-command"))


;
; Interact with Promise without external library
;

; use ^js type-hint to access js properties, 
; in this case, keep the compiler happy to remove the warning 
; "Cannot infer target type in expression (. e -uuid)"
(defn block-context-menu-item-action [^js/IHookEvent e]
  (js/console.log "Hello from block-context-menu-item-action %s" (.-uuid e)))

(defn get-page [^js/BlockEntity block]
  (let [page_id (.-page block)]
    (js/console.log block)
    (-> (js/logseq.Editor.getPage (.-id page_id))
        (.then js/console.log))))

; use core.async to make sure async codes are executed.
(defn block-context-menu-get-all-blocks-on-the-page [^js/IHookEvent e]
    (-> (js/logseq.Editor.getBlock (.-uuid e))
        (.then get-page)))

(defn main []
  ; print to console
  (js/console.log "Hello from featha main")
  ; show message in logseq UI
  (js/logseq.App.showMsg "❤️  Message from featha main")
  ; register slash command
  (js/logseq.Editor.registerSlashCommand "featha" slash-command)
  ; register block context menu item use fn.
  (js/logseq.Editor.registerBlockContextMenuItem "featha" (fn [e] (block-context-menu-item-action e)))
  ; register block context menu to process block
  (js/logseq.Editor.registerBlockContextMenuItem "featha (block)" (fn [e] (block-context-menu-get-all-blocks-on-the-page e))))

(defn init []
  (-> (js/logseq.ready) ; return a promise
      (.then main)      ; then on the promise
      (.catch #(js/console.error))))