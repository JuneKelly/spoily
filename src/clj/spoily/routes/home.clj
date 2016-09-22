(ns spoily.routes.home
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET]]
            [ring.util.http-response :as response]
            [compojure.route :as route]
            [clojure.java.io :as io]))

(defn home-page []
  (layout/render
    "home.html" {:page-title "Spoily"}))

(defn about-page []
  (layout/render "about.html"))

(defroutes home-routes
  (GET "/" [] (home-page))
  (route/resources "/static")
  (GET "/about" [] (about-page)))
