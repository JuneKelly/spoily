(ns spoily.routes.spoilers
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [spoily.db.core :as db]
            [spoily.util :refer [iso-now]]
            [ring.util.response :as response]))


(defn create-spoiler [req]
  (let [spoiler-text (get-in req [:params :spoilerText])
        mask-text    (get-in req [:params :maskText])
        new-spoiler-id (db/save-spoiler {:spoilerText spoiler-text
                                           :maskText    mask-text
                                           :created     (iso-now)})]
    (log/info (str "create spoiler: " new-spoiler-id))
    (response/redirect (str "/spoiler/" new-spoiler-id))))


(defn view-spoiler [id]
  (let [spoiler (db/get-spoiler id)]
    (if (nil? spoiler)
      (response/not-found)
      (layout/render "spoiler.html" {:page-title "SPOILER"
                                     :spoiler spoiler}))))


(defroutes spoiler-routes
  (GET "/spoiler/:id" [id] (view-spoiler id))
  (POST "/spoiler" [] create-spoiler))
