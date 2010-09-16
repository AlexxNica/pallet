(ns pallet.target
  "Provide information about the target image"
  (:require
   [org.jclouds.compute :as jclouds]
   [clojure.contrib.condition :as condition]))

(defn os-family
  "OS family"
  [target] (:os-family target))

(defn admin-group
  "Default administrator group"
  [target]
  (case (os-family target)
    :yum "wheel"
    "adm"))

(defn packager
  "Package manager"
  [target]
  (or
   (:packager target)
   (let [os-family (:os-family target)]
     (cond
      (#{:ubuntu :debian :jeos :fedora} os-family) :aptitude
      (#{:centos :rhel} os-family) :yum
      (#{:arch} os-family) :pacman
      (#{:suse} os-family) :zypper
      (#{:gentoo} os-family) :portage
      (#{:darwin} os-family) :brew
      :else (condition/raise
             :type :unknown-packager
             :message (format
                       "Unknown packager for %s : :image %s"
                       os-family target))))))
