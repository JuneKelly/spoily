(ns spoily.util
  (:require [clj-time.coerce :as coerce]
            [clj-time.core :as time]))

(defn iso-now []
  (-> (time/now)
      (coerce/to-string)))
