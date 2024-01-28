(ns featha.app
  (:require
   ["@logseq/libs"]))

(defn slash-command [] 
  (js/console.log "Hello from featha slash-command"))

(defn block-context-menu-item-action []
  (js/console.log "Hello from featha block-context-menu-item-action"))

(defn main []
  (js/console.log "Hello from featha main")
  (js/logseq.App.showMsg "❤️  Message from featha main")
  (js/logseq.Editor.registerSlashCommand "featha" slash-command)
  (js/logseq.Editor.registerBlockContextMenuItem "featha" block-context-menu-item-action))

(defn init []
  (-> (js/logseq.ready) ; return a promise
      (.then main)      ; then on the promise
      (.catch #(js/console.error))))