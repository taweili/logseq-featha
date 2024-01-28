(ns featha.app
  (:require
   ["@logseq/libs"]))

(defn main []
  (js/console.log "Hello from featha main")
  (js/logseq.App.showMsg "❤️  Message from featha main"))

(defn init []
  (-> (js/logseq.ready)
      (.then main)
      (.catch #(js/console.error))))