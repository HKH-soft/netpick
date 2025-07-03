'use client'

import { useEffect } from 'react'
import { Formik, Form, useField ,useFormikContext } from 'formik';
import * as Yup from 'yup';
import { XCircleIcon } from '@heroicons/react/20/solid';
import { ChevronDownIcon } from '@heroicons/react/16/solid'
import { saveCustomer } from '../../services/client';

const MyTextInput = ({ label, ...props }) => {
// useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
// which we can spread on <input>. We can use field meta to show an error
// message if the field is invalid and it has been touched (i.e. visited)
const [field, meta] = useField(props);
return (
    <>
    <div className={`sm:col-span-${props.gridsize}`}>
        <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-gray-900">
            {label}
        </label>
        <div className="mt-2">
            <input
            {...field} {...props}
            className="text-input block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
            />
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
    </>
);
};

const MySelect = ({ label, ...props }) => {
const [field, meta] = useField(props);
return (
    <div className={`sm:col-span-${props.gridsize}`}>
        <div>
            <label htmlFor={props.id || props.name} className="block text-sm/6 font-medium text-gray-900">
                {label}
            </label>
            <div className="mt-2 grid grid-cols-1">
                <select
                {...field} {...props}
                className="col-start-1 row-start-1 w-full appearance-none rounded-md bg-white py-1.5 pr-8 pl-3 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
                >
                </select>
                <ChevronDownIcon
                aria-hidden="true"
                className="pointer-events-none col-start-1 row-start-1 mr-2 size-5 self-center justify-self-end text-gray-500 sm:size-4"
                />
            </div>
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
);
};

// And now we can use these
const CreateCustomerForm = ({fetchCustomers, setDisable, addNotification}) => {
    
    const FormDisabler = () => {
        const { isSubmitting, isValid, dirty } = useFormikContext();
        
        useEffect(() => {
            setDisable(isSubmitting || !(dirty && isValid));
        }, [isSubmitting, isValid, dirty, setDisable]);
        
        return null;
    };

return (
    <Formik
        initialValues={{
        firstName: '',
        lastName: '',
        email: '',
        age: '',
        gender: '', 
        }}
        validationSchema={Yup.object({
        firstName: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces')
            .required('Required'),
        lastName: Yup.string()
            .max(20, 'Must be 20 characters or less')
            .matches(/^[a-zA-Z\s]*$/, 'Name can only contain English letters and spaces')
            .required('Required'),
        email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
        gender: Yup.string()
            .oneOf(
            ['Female','Male'],
            'Invalid Gender'
            )
            .required('Required'),
        age: Yup.number()
            .max(100,'Must be 100 years or less')
            .min(16,'Must be 16 years or more')
            .required('Required'),
        })}
        onSubmit={({ firstName, lastName, gender, ...rest }, { setSubmitting }) => {
            const customer = rest;
            customer.name = `${firstName.trim()} - ${lastName.trim()}`;
            customer.gender = gender === "Male" ? true : false ;
            setSubmitting(true);
            
            saveCustomer(customer)
                .then(res => {
                    addNotification(`${customer.name} was successfully saved!`);
                    fetchCustomers();
                })
                .catch(err => {
                    addNotification(err.response.data.message, 'error');
                })
                .finally(() => {
                    setSubmitting(false);
                })
        }}
    >
        <div>
            <FormDisabler />
            <Form
                id='createCustomer'
                className='grid max-w-2xl grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6'
            >
            <MyTextInput
                gridsize="3"
                label="First Name"
                name="firstName"
                type="text"
                placeholder="Jane"
            />

            <MyTextInput
                gridsize="3"
                label="Last Name"
                name="lastName"
                type="text"
                placeholder="Doe"
            />

            <MyTextInput
                gridsize="full"
                label="Email Address"
                name="email"
                type="email"
                placeholder="jane@formik.com"
            />

            <MySelect label="Gender" name="gender" gridsize="4">
                <option value="">Select Gender</option>
                <option value="Female">Female</option>
                <option value="Male">Male</option>
            </MySelect>

            <MyTextInput
                gridsize="2"
                label="Age"
                name="age"
                type="text"
                placeholder="21"
            />

            </Form>
        </div>
    </Formik>
);
};

export default CreateCustomerForm;