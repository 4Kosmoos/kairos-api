# 🎯 Kairos – Calendrier collaboratif pour désorganisés chroniques

## 📌Résumé du projet

Kairos est une application permettant à un groupe d’amis de mieux s’organiser en affichant leurs disponibilités, en proposant des sorties et en facilitant la prise de décision grâce à un système de confirmation de présence.

Les utilisateurs utiliseront une application Ionic connectée à une API Java spring.

L’API sera auto-hébergée sur un Raspberry Pi.

Ce README concernera la partie API.

Kairos est un concept grec signifiant le bon moment, l'occasion parfaite...

## 🌐 Fonctionnalités via l'API (pour les users)

- [ ] se connecter
- [ ] modifier son profil
- [ ] modifier son mot de passe (ou le réinitialiser via mail)
- [ ] créer, modifier et supprimer ses disponibilités 
- [ ] voir les disponibilités des autres
- [ ] créer, modifier et supprimer leurs sorties
- [ ] marquer présent ou annuler sa présence pour une sortie
- [ ] commenter des sorties

## 🔧 Objets :

### 🔹 User
- [ ] mail
- [ ] prénom/pseudo
- [ ] icone
- [ ] rôle
- [ ] mdp
- [ ] citation
- [ ] liste de disponibilités 
- [ ] une couleur

### 🔹 Disponibilité
- [ ] user
- [ ] date ou liste de dates
- [ ] commentaires (du user)

### 🔹 Sortie
- [ ] date/groupe de date
- [ ] titre
- [ ] description
- [ ] liste de user (présents)
- [ ] user (créateur)
- [ ] commentaires (dictionaire string commentaire et user commentateur)