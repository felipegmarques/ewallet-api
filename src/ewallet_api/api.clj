(ns ewallet-api.api
  (:require [ewallet-api.domain :refer :all]
            [ewallet-api.data :refer :all]))

(def file-path (str (System/getProperty "user.dir") "/data/entries.csv"))

(defn get-entries-by-period
    "Get entries for the specified period"
    ([start-date end-date] (get-entries-by-period file-path start-date end-date))
    ([file-path start-date end-date]
        (let [entries (parse-raw-entries (slurp file-path))]
            (get-entries start-date end-date entries))
    ))

(defn get-summary-by-period
    "Get period summary"
    ([start-date end-date] (get-summary-by-period file-path start-date end-date))
    ([file-path start-date end-date]
        (let [entries (parse-raw-entries (slurp file-path))]
            (summarize-period start-date end-date entries))))

(defn save-entry
    "Add entry to file"
    ([entry] (save-entry entry file-path))
    ([entry file-path] (save-entry entry file-path file-path))
    ([entry origin-file-path destination-file-path]
        (let [entries (parse-raw-entries (slurp origin-file-path))
            new-entries (add-entry entries entry)]
            (spit destination-file-path (entries-to-csv new-entries)))
        ))