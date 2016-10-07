(ns spoily.routes.spoilers
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [spoily.db.core :as db]
            [spoily.util :refer [iso-now]]
            [crypto.random :as random]
            [ring.util.response :as response]))


(defn create-spoiler [req]
  (let [spoiler-text (get-in req [:params :spoilerText])
        mask-text    (get-in req [:params :maskText])
        topic        (get-in req [:params :topic])
        new-spoiler-slug (random/url-part 9)
        new-spoiler-id (db/save-spoiler {:_id         new-spoiler-slug
                                         :spoilerText spoiler-text
                                         :maskText    mask-text
                                         :topic       topic
                                         :created     (iso-now)})]
    (log/info (str "create spoiler: " new-spoiler-slug))
    (response/redirect (str "/s/" new-spoiler-slug))))


(defn view-spoiler [slug]
  (let [spoiler (db/get-spoiler slug)]
    (if (nil? spoiler)
      (response/not-found)
      (do
        (log/info (str "view spoiler: " slug))
        (layout/render "spoiler.html" {:page-title "Spoiler Warning!"
                                      :spoiler spoiler})))))


(defroutes spoiler-routes
  (POST "/spoiler" [] create-spoiler)
  (GET "/s/:slug" [slug] (view-spoiler slug)))
