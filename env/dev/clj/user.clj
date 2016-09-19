(ns user
  (:require [mount.core :as mount]
            spoily.core))

(defn start []
  (mount/start-without #'spoily.core/repl-server))

(defn stop []
  (mount/stop-except #'spoily.core/repl-server))

(defn restart []
  (stop)
  (start))


