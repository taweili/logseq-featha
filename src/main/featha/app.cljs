(ns featha.app
  (:require
   ["@logseq/libs"]))

(defn slash-command [] 
  (js/console.log "Hello from slash-command"))

; use ^js type-hint to access js properties, 
; in this case, keep the compiler happy to remove the warning 
; "Cannot infer target type in expression (. e -uuid)"
(defn block-context-menu-item-action [^js/IHookEvent e]
  (js/console.log "Hello from block-context-menu-item-action %s" (.-uuid e)))

(defn main []
  ; print to console
  (js/console.log "Hello from featha main")
  ; show message in logseq UI
  (js/logseq.App.showMsg "❤️  Message from featha main")
  ; register slash command
  (js/logseq.Editor.registerSlashCommand "featha" slash-command)
  ; register block context menu item use fn.
  (js/logseq.Editor.registerBlockContextMenuItem "featha" (fn [e] (block-context-menu-item-action e))))

(defn init []
  (-> (js/logseq.ready) ; return a promise
      (.then main)      ; then on the promise
      (.catch #(js/console.error))))