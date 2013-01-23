(ns com.thelastcitadel.apropos
  (:require [clj-http.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]))

(def vowels #{\a \e \i \o \u})

(defn f [result]
  (let [one (rand-nth (filter :notable result))
        [subject info] ((juxt :name (comp :name :notable)) one)
        article (if (contains? vowels (Character/toLowerCase (first info)))
                  "an"
                  "a")]
    (str subject " is " article " " info
         ". (http://www.freebase.com/view/" (:mid one) ")")))

(defn apropos [noun]
  (f
   (:result
    (json/decode
     (:body
      (http/get "https://www.googleapis.com/freebase/v1/search"
                {:query-params {:query noun}
                 :insecure? true}))
     true))))
