(ns ewallet-api.core
  (:require 
    [ewallet-api.routes :refer [app-routes]]
    [ring.middleware.json :refer [wrap-json-response]]
    [compojure.core :refer :all]
    [compojure.handler :refer [api]])
  (:gen-class))

(defn wrap-exception [handler]
  (fn [request]
    (try
      (handler request)
      (catch clojure.lang.ExceptionInfo e
        {:status 400 :body (.getMessage e)}))))

(def app (-> app-routes (api) (wrap-exception) (wrap-json-response)))
