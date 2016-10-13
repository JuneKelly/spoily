(ns spoily.routes.home
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.response :as response]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defn home-page [req]
  (layout/render
    "home.html" {:page-title "Spoily" :flash (req :flash)}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" req (home-page req))
  (route/resources "/static")
  (GET "/about" [] (about-page)))
