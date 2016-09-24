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
        new-spoiler-slug (random/url-part 9)
        new-spoiler-id (db/save-spoiler {:spoilerText spoiler-text
                                         :maskText    mask-text
                                         :slug        new-spoiler-slug
                                         :created     (iso-now)})]
    (log/info (str "create spoiler: " new-spoiler-id " - " new-spoiler-slug))
    (response/redirect (str "/s/" new-spoiler-slug))))


(defn view-spoiler [slug]
  (let [spoiler (db/get-spoiler-by-slug slug)]
    (if (nil? spoiler)
      (response/not-found)
      (layout/render "spoiler.html" {:page-title "SPOILER"
                                     :spoiler spoiler}))))


(defroutes spoiler-routes
  (POST "/spoiler" [] create-spoiler)
  (GET "/s/:slug" [slug] (view-spoiler slug)))
