(ns spoily.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[spoily started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[spoily has shut down successfully]=-"))
   :middleware identity})
