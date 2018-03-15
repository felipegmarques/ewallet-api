(ns ewallet-api.core
  (:require 
    [ewallet-api.routes :refer [app-routes]]
    [ring.adapter.jetty :refer :all]
    [ring.util.response :refer [response]]
    [ring.middleware.reload :refer [wrap-reload]]
    [ring.middleware.json :refer [wrap-json-response]]
    [compojure.core :refer :all]
    [compojure.handler :refer [api]]
    [compojure.route :as route]
    [clj-time.core :as time]
    [clj-time.format :as f]
    [ewallet-api.api :refer :all])
  (:gen-class))

(defn wrap-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        {:status 400 :body (.getMessage e)}))))

(def app (-> app-routes (api) (wrap-exception) (wrap-json-response)))
