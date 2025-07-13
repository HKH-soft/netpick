'use client'

import { useState } from 'react';
import Alert from "./Alert"
import UpdateModal from './UpdateCustomerForm';
import { getCustomer } from '../../services/client';

export default function Card(props) {
  const [showAlert, setShowAlert] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);

  return (
        <div key={props.id} className="group relative">
          {showAlert && <Alert customerId={props.href} onClose={() => setShowAlert(false)} removeCustomer={props.removeCustomer} customerName={props.name}/>}
          {showUpdateModal && <UpdateModal pastCustomer={props} onClose={() => setShowUpdateModal(false)} addNotification={props.addNotification} fetchCustomers={props.fetchCustomers} />}
          <div className="relative rounded-lg">
            <img
              alt={props.imageAlt}
              src={props.imageSrc}
              className="aspect-4/3 w-full rounded-lg bg-zinc-100 object-cover"
            />
            <div
              aria-hidden="true"
              className="absolute inset-0 flex gap-x-3 items-end p-4 bg-white/20 opacity-0 group-hover:opacity-100  transform shadow-xl transition-all data-closed:translate-y-4 data-closed:opacity-0 data-enter:duration-300 data-enter:ease-out "
            >
              <button onClick={() => setShowAlert(true)} className="w-full rounded-md bg-red-500/75 px-4 py-2 text-center text-sm font-medium text-zinc-900 backdrop-blur-sm backdrop-filter">
                Delete Customer
              </button>
              <button onClick={() => setShowUpdateModal(true)} className="w-full rounded-md bg-white/75 px-4 py-2 text-center text-sm font-medium text-zinc-900 backdrop-blur-sm backdrop-filter">
                Update Customer
              </button>
            </div>
          </div>
          <div className="mt-4 flex items-center justify-between space-x-8 text-base font-medium text-zinc-900">
            <h3>
              {props.name} | {props.gender == false ? 'Female' : 'Male'}
            </h3>
            <p>Age: {props.age}</p>
          </div>
          <p className="mt-1 text-sm text-zinc-500">{props.email}</p>
        </div>
  )
}
