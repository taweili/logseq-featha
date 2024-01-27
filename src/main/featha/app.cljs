(ns featha.app
  (:require
   ["@logseq/libs"]
   [promesa.core :as p]))

(defn main []
  (js/console.log "Hello from featha main")
  (js/logseq.App.showMsg "❤️  Message from featha main"))

(defn init []
  (-> (p/promise (js/logseq.ready))
      (p/then main)
      (p/catch #(js/console.error))))
