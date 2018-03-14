(ns ewallet-api.domain
    (:require [clj-time.core :as time]))


(defn summarize-period
    "Get the entries and produces summary"
    [start-date end-date entries]
    (reduce 
        (fn [acc {:keys [value date]}]
            (let [belong-to-period (and (time/after? date start-date) (time/before? date end-date))
                transformed-entry {
                    :current-amount (if (time/before? date end-date) value 0)
                    :credits (if belong-to-period (max 0 value) 0)
                    :debits  (if belong-to-period (min 0 value) 0)
                    :previous-amount (if (time/before? date start-date) value 0)
                    }]
                (merge-with + acc transformed-entry))
        ) 
        { :current-amount 0.0 :previous-amount 0.0 :debits 0.0 :credits 0.0 }
        entries))

(defn get-entries
    "Get the entries according to date"
    [start-date end-date entries]
    (filter 
        (fn [{:keys [date]}] (and (time/after? date start-date) (time/before? date end-date))) 
        entries))

(defn add-entry
    "Add entry to entries"
    [entries entry]
    (conj entries entry))