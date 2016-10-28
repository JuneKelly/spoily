(ns spoily.routes.home
  (:require [spoily.layout :as layout]
            [compojure.core :refer [defroutes GET POST]]
            [compojure.route :as route]
            [clojure.tools.logging :as log]
            [spoily.config :refer [env]]
            [spoily.db.core :as db]
            [spoily.util :refer [iso-now]]
            [crypto.random :as random]
            [bouncer.core :as bouncer]
            [bouncer.validators :as v]
            [ring.util.response :as response]))

(defn health-check [req]
  (let [db-ok (db/health-check)]
    (if db-ok
      (do
        (log/info "Health-check success")
        {:status 200,
        :headers {"Content-Type" "text/plain"}
        :body "ok"})
      (do
        (log/error "Health-check failed")
        {:status 500,
        :headers {"Content-Type" "text/plain"}
        :body "failed"}))))


(defn render-home-page [options]
  (let [site-name (or (:site-name env) "spoily")]
    (layout/render "home.html"
                  (merge {:page-title (str site-name ", share spoilers") :error nil :spoiler nil}
                          options))))

(defn home-page [req]
  (log/info "View home page")
  (render-home-page {}))


(defn about-page []
  (layout/render "about.html"))


(defn valid-spoiler? [spoiler]
  (bouncer/valid?
   spoiler
   :spoilerText [v/required [v/min-count 2] [v/max-count 10000]]
   :maskText    [v/required [v/min-count 2] [v/max-count 512]]
   :topic       [[v/max-count 256]]))


(defn create-spoiler [req]
  (let [spoiler (select-keys (req :params) [:spoilerText :maskText :topic])]
    (if (valid-spoiler? spoiler)
      (let [new-spoiler-slug (random/url-part 9)]
        (-> spoiler
            (merge {:_id new-spoiler-slug
                    :created (iso-now)
                    :topic (let [topic (:topic spoiler)]
                             (if (= topic "")
                               nil
                               topic))})
            (db/save-spoiler))
        (log/info (str "Create spoiler: " new-spoiler-slug))
        (response/redirect (str "/s/" new-spoiler-slug)))
      (do
        (log/info (str "Validation failed: "
                       (clojure.string/join
                        ","
                        (map (fn [[k, v]] (str k "=" (count v)))
                             spoiler))))
        (render-home-page {:error "Not a valid spoiler"
                           :spoiler spoiler})))))


(defn view-spoiler [slug]
  (let [spoiler (db/get-spoiler slug)]
    (if (nil? spoiler)
      (response/not-found)
      (do
        (log/info (str "View spoiler: " slug))
        (layout/render "spoiler.html" {:page-title "Spoiler Warning!"
                                      :spoiler spoiler})))))


(defn sitemap-xml [req]
  (let [base-url (or (:base-url env) "http://localhost:3000")]
    {:status 200,
     :headers {"Content-Type" "text/xml"}
     :body
     (str
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>
       <urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">
         <url>
           <loc>" base-url "</loc>
         </url>
         <url>
           <loc>" base-url "/about</loc>
         </url>
       </urlset>")}))


(defroutes home-routes
  (GET "/" req (home-page req))
  (GET "/status" [] "spoily is up")
  (GET "/health_check" req (health-check req))
  (GET "/about" [] (about-page))
  (GET "/sitemap.xml" req (sitemap-xml req))
  (route/resources "/static")
  (POST "/spoiler" [] create-spoiler)
  (GET "/spoiler" [] (response/redirect "/"))
  (GET "/s/:slug" [slug] (view-spoiler slug)))
