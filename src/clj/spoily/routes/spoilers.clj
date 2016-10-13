(ns spoily.routes.spoilers
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [clojure.tools.logging :as log]
            [spoily.db.core :as db]
            [spoily.util :refer [iso-now]]
            [crypto.random :as random]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [ring.util.response :as response]))


(defn valid-spoiler? [spoiler]
  (bouncer/valid?
   spoiler
   :spoilerText [v/required [v/min-count 2] [v/max-count 10000]]
   :maskText    [v/required [v/min-count 2] [v/max-count 512]]
   :topic       [[v/min-count 2] [v/max-count 256]]))


(defn create-spoiler [req]
  (let [spoiler (select-keys (req :params) [:spoilerText :maskText :topic])]
    (if (valid-spoiler? spoiler)
      (let [new-spoiler-slug (random/url-part 9)]
        (-> {:_id new-spoiler-slug
             :created (iso-now)}
            (merge spoiler)
            (db/save-spoiler))
        (log/info (str "create spoiler: " new-spoiler-slug))
        (response/redirect (str "/s/" new-spoiler-slug)))
      (do
        (log/info (str "validation failed"))
        (layout/render "home.html"
                       {:page-title "Spoily"
                        :error "Not a valid spoiler"
                        :spoiler spoiler})))))


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
