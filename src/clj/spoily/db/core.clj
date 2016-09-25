(ns spoily.db.core
  (:require
    [cheshire.core :refer [generate-string parse-string]]
    [clojure.java.jdbc :as jdbc]
    [conman.core :as conman]
    [spoily.config :refer [env]]
    [mount.core :refer [defstate]]
    [bedquilt.core :as bq]))

(defstate ^:dynamic *db*
           :start (conman/connect! {:jdbc-url (env :database-url)})
           :stop (conman/disconnect! *db*))


(defn get-spoiler [id]
  (bq/find-one-by-id *db* "spoilers" id))


(defn save-spoiler [spoiler]
  (bq/save *db* "spoilers" spoiler))
